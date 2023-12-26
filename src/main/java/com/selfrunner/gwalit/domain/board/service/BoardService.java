package com.selfrunner.gwalit.domain.board.service;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.selfrunner.gwalit.domain.board.dto.request.PostBoardReq;
import com.selfrunner.gwalit.domain.board.dto.request.PutBoardReq;
import com.selfrunner.gwalit.domain.board.dto.request.ReplyReq;
import com.selfrunner.gwalit.domain.board.dto.response.*;
import com.selfrunner.gwalit.domain.board.entity.Board;
import com.selfrunner.gwalit.domain.board.entity.File;
import com.selfrunner.gwalit.domain.board.entity.Reply;
import com.selfrunner.gwalit.domain.board.enumerate.BoardCategory;
import com.selfrunner.gwalit.domain.board.enumerate.QuestionStatus;
import com.selfrunner.gwalit.domain.board.exception.BoardException;
import com.selfrunner.gwalit.domain.board.repository.BoardRepository;
import com.selfrunner.gwalit.domain.board.repository.FileJdbcRepository;
import com.selfrunner.gwalit.domain.board.repository.FileRepository;
import com.selfrunner.gwalit.domain.board.repository.ReplyRepository;
import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.domain.member.entity.*;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.domain.member.repository.MemberAndNotificationJdbcRepository;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.domain.notification.entity.Notification;
import com.selfrunner.gwalit.domain.notification.repository.NotificationRepository;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.aws.S3Client;
import com.selfrunner.gwalit.global.util.fcm.FCMClient;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final FileRepository fileRepository;
    private final FileJdbcRepository fileJdbcRepository;
    private final MemberRepository memberRepository;
    private final LessonRepository lessonRepository;
    private final MemberAndLectureRepository memberAndLectureRepository;
    private final NotificationRepository notificationRepository;
    private final MemberAndNotificationJdbcRepository memberAndNotificationJdbcRepository;
    private final S3Client s3Client;
    private final FCMClient fcmClient;

    @Transactional
    public BoardRes registerBoard(Member member, List<MultipartFile> multipartFileList, PostBoardReq postBoardReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, postBoardReq.getLectureId()).orElseThrow(() -> new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        // 파일 개수 최대 5개 제한 로직 && 클래스당 용량 검사 로직 추가 필요
        if(multipartFileList != null && checkFileCapacity(memberAndLecture.getLecture().getLectureId(), multipartFileList, null)) {
            throw new BoardException(ErrorCode.TOTAL_SIZE_EXCEED);
        }
        if(multipartFileList != null && multipartFileList.size() > 5) {
            throw new BoardException(ErrorCode.TOTAL_FILE_EXCEED);
        }

        // Business Logic
        Board board = postBoardReq.toEntity(memberAndLecture.getLecture(), member);
        Board saveBoard = boardRepository.save(board);
        LocalDate lessonDate = (board.getLessonId() != null) ? lessonRepository.findLessonDateByLessonId(board.getLessonId()).orElse(null) : null;
        // File이 존재하면 S3 업로드 진행
        List<FileRes> fileUrlList = (multipartFileList != null) ? uploadFileList(multipartFileList, member.getMemberId(), memberAndLecture.getLecture().getLectureId(), saveBoard.getBoardId(), null): null;
        // FCM 알림 전송
        sendBoardNotification(member, memberAndLecture, board);

        // Response
        return new BoardRes(saveBoard, member, lessonDate, fileUrlList);
    }

    @Transactional
    public BoardRes updateBoard(Member member, Long boardId, List<MultipartFile> multipartFileList, PutBoardReq putBoardReq) {
        // Validation
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(!board.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        // 파일 용량 확인 로직 필요 (1. 각 파일당 5MB 이하인지 2.삭제 후 남은 용량에서 해당 파일들 넣을 수 있는 용량이 남아있는지)
        if(multipartFileList != null && checkFileCapacity(board.getLecture().getLectureId(), multipartFileList, putBoardReq.getDeleteFileList())) {
            throw new BoardException(ErrorCode.TOTAL_SIZE_EXCEED);
        }
        if(multipartFileList != null && (multipartFileList.size() > 5 || (multipartFileList.size() - putBoardReq.getDeleteFileList().size() + fileRepository.findCountByBoardId(boardId) > 5))) {
            throw new BoardException(ErrorCode.TOTAL_FILE_EXCEED);
        }

        // Business Logic (삭제되는 파일 지우고, 업로드할 파일 만들기)
        deleteFileList(putBoardReq.getDeleteFileList());
        board.update(putBoardReq);
        Board updateBoard = boardRepository.save(board);
        LocalDate lessonDate = (board.getLessonId() != null) ? lessonRepository.findLessonDateByLessonId(board.getLessonId()).orElse(null) : null;
        List<FileRes> fileResList = (multipartFileList != null) ? uploadFileList(multipartFileList, member.getMemberId(), board.getLecture().getLectureId(), boardId, null) : null;

        // Response
        return new BoardRes(updateBoard, member, lessonDate, fileResList);
    }

    @Transactional
    public void deleteBoard(Member member, Long boardId) {
        // Validation
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(!board.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic (file 버켓에서 삭제 + DB에서 삭제)
        List<String> fileUrlList = fileRepository.findUrlListByBoardId(boardId);
        deleteFileList(fileUrlList);
        replyRepository.deleteAllByBoardBoardId(boardId);
        boardRepository.delete(board);

        // Response
    }

    @Transactional
    public BoardRes changeQuestionStatus(Member member, Long boardId) {
        // Validation
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException(ErrorCode.NOT_FOUND_EXCEPTION));
        // 질문 형태의 게시글이 아닌 경우
        if(board.getStatus().equals(QuestionStatus.GENERAL)) {
            throw new BoardException(ErrorCode.NOT_QUESTION_STATUS);
        }
        // 작성자와 선생님이 아니라면 상태 수정 불가능하도록 제약 조건 삽입
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, board.getLecture().getLectureId()).orElseThrow(() -> new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        if(!board.getMember().getMemberId().equals(member.getMemberId()) && memberAndLecture.getIsTeacher().equals(Boolean.FALSE)) {
            throw new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        board.changeQuestionStatus();
        Board updateBoard = boardRepository.save(board);
        LocalDate lessonDate = (board.getLessonId() != null) ? lessonRepository.findLessonDateByLessonId(board.getLessonId()).orElse(null) : null;
        List<FileRes> fileList = fileRepository.findAllByBoardId(boardId).orElse(null);

        // Response
        return new BoardRes(updateBoard, updateBoard.getMember(), lessonDate, fileList);
    }

    public BoardReplyRes getOneBoard(@Auth Member member, Long boardId) {
        // Validation
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        // 게시글 공개 여부와 권한 여부 확인
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, board.getLecture().getLectureId()).orElseThrow(() -> new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        if(board.getIsPublic().equals(Boolean.FALSE) && !board.getMember().getMemberId().equals(member.getMemberId()) && memberAndLecture.getIsTeacher().equals(Boolean.FALSE)) {
            throw new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        LocalDate lessonDate = (board.getLessonId() != null) ? lessonRepository.findLessonDateByLessonId(board.getLessonId()).orElse(null) : null;
        List<FileRes> fileList = fileRepository.findAllByBoardId(boardId).orElse(null);
        Integer replyCount = replyRepository.findReplyCountByBoardId(boardId);

        // Response
        return new BoardReplyRes(board, board.getMember(), lessonDate, replyCount, fileList);
    }

    public Slice<BoardMetaRes> getBoardPagination(Member member, Long lectureId, String category, Long cursor, Pageable pageable) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        LocalDateTime cursorCreatedAt = (cursor != null) ? boardRepository.findById(cursor).map(BaseTimeEntity::getCreatedAt).orElse(null) : null;

        // Response
        return boardRepository.findBoardPaginationByCategory(member.getMemberId(), lectureId, (!category.equals("ALL")) ? BoardCategory.valueOf(category) : null, cursor, cursorCreatedAt, pageable);
    }

    public List<BoardMetaRes> getOpenQuestion(Member member) {
        // Validation

        // Business Logic && response
        return boardRepository.findUnsolvedBoardResByMemberId(member.getMemberId());
    }

    @Transactional
    public ReplyRes registerReply(Member member, Long boardId, List<MultipartFile> multipartFileList, ReplyReq replyReq) {
        // Validation
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException(ErrorCode.NOT_FOUND_EXCEPTION));
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, board.getLecture().getLectureId()).orElseThrow(() -> new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        // TODO: 공개 비공개 여부에 따른 검증 로직 추가 필요
        // 클래스당 용량 검사 로직 추가
        if(multipartFileList != null && checkFileCapacity(board.getLecture().getLectureId(), multipartFileList, null)) {
            throw new BoardException(ErrorCode.TOTAL_SIZE_EXCEED);
        }
        if(multipartFileList != null && multipartFileList.size() > 5) {
            throw new BoardException(ErrorCode.TOTAL_FILE_EXCEED);
        }

        // Business Logic
        Reply reply = replyReq.toEntity(board, member);
        Reply saveReply = replyRepository.save(reply);
        // File이 존재하면 S3 업로드
        List<FileRes> fileUrlList = (multipartFileList != null) ? uploadFileList(multipartFileList, member.getMemberId(), memberAndLecture.getLecture().getLectureId(), boardId, saveReply.getReplyId()): null;
        // 게시글 작성자와 댓글 작성자가 다르면 알림 발송
        if(!board.getMember().getMemberId().equals(member.getMemberId())) {
            sendReplyNotification(member, memberAndLecture, board, reply);
        }

        // Response
        return new ReplyRes(saveReply, saveReply.getMember(), fileUrlList);
    }

    @Transactional
    public void deleteReply(Member member, Long boardId, Long replyId) {
        // Validation
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new BoardException(ErrorCode.NOT_FOUND_EXCEPTION));
        // 댓글 작성자가 아니거나 다른 게시글 ID로 요청했는지 확인하는 조건문
        if(!reply.getMember().getMemberId().equals(member.getMemberId()) || !reply.getBoard().getBoardId().equals(boardId)) {
            throw new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic (댓글 및 연관 파일 삭제)
        replyRepository.delete(reply);
        List<String> fileUrlList = fileRepository.findUrlListByReplyId(replyId);
        deleteFileList(fileUrlList);

        // Response
    }

    public Slice<ReplyRes> getReplyPagination(Member member, Long boardId, Long cursor, Pageable pageable) {
        // Validation
        // TODO: 요청자가 해당 게시글을 포함한 클래스에 소속된 사람인지 검증 필요
        boardRepository.findBoardByMemberIdAndBoardId(member.getMemberId(), boardId).orElseThrow(() -> new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        LocalDateTime cursorCreatedAt = (cursor != null) ? replyRepository.findById(cursor).map(BaseTimeEntity::getCreatedAt).orElse(null) : null;

        // Response
        return replyRepository.findReplyPaginationByBoardId(boardId, cursor, cursorCreatedAt, pageable);
    }

    public BoardFileRes getFileCapacity(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        Long fileCapacity = fileRepository.findCapacityByLectureId(lectureId);

        return new BoardFileRes(lectureId, fileCapacity);
    }

    public List<BoardMetaRes> getBoardMetaList(Member member, Long lessonId) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new BoardException(ErrorCode.NOT_FOUND_EXCEPTION));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic && Response
        return boardRepository.findBoardMetaListByLessonId(lessonId);
    }

    /**
     * 게시글 및 댓글에서 등록한 파일 리스트들을 S3 업로드 및 File DB에 저장하는 메소드
     * @param multipartFileList - 입력받은 파일 리스트
     * @param memberId - 게시자 ID
     * @param lectureId - 게시판과 연결된 클래스 ID (용량 파악을 위해 사용)
     * @param boardId - 게시글에 등록한 파일을 조회할 때 사용하는 게시글 ID (nullable)
     * @param replyId - 댓글로 등록한 파일을 조회할 때 사용하게 되는 댓글 ID (nullable)
     * @return - 등록된 파일 주소 리스트 반환 (FileRes 구조)
     */
    private List<FileRes> uploadFileList(List<MultipartFile> multipartFileList, Long memberId, Long lectureId, Long boardId, Long replyId) {
        List<FileRes> fileUrlList = new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        multipartFileList.forEach(
                multipartFile -> {
                    try {
                        String url = s3Client.upload(multipartFile, "board/" + lectureId);
                        File file = File.builder()
                                .name(multipartFile.getOriginalFilename())
                                .url(url)
                                .size(multipartFile.getSize())
                                .memberId(memberId)
                                .lectureId(lectureId)
                                .boardId(boardId)
                                .replyId(replyId)
                                .build();
                        fileList.add(file);
                        fileUrlList.add(new FileRes(file.getName(), file.getUrl(), file.getSize()));
                    } catch (Exception e) {
                        throw new BoardException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
                    }
                }
        );

        fileJdbcRepository.saveAll(fileList);

        return fileUrlList;
    }

    /**
     * 파일링크리스트를 입력받아, 해당 파일들 S3에서 삭제 및 DB 논리적 삭제하는 메소드
     * @param fileUrlList - 파일링크리스트(String)
     */
    private void deleteFileList(List<String> fileUrlList) {
        fileUrlList.forEach(
                fileUrl -> {
                    try {
                        s3Client.delete(fileUrl);
                    } catch (Exception e) {
                        throw new BoardException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
                    }
                }
        );

        fileRepository.deleteAllByUrlIn(fileUrlList);
    }

    /**
     * 특정 클래스의 사용되고 있는 자료실 용량 확인하여 추가 등록이 가능한지를 확인하는 메소드
     * @param lectureId - 클래스 ID
     * @param inputFileList - 추가될 파일리스트
     * @param deleteFileUrlList - 삭제될 파일리스트
     * @return - 자료실 용량 여유분 존재 시 true, 미존재시 false
     */
    private boolean checkFileCapacity(Long lectureId, List<MultipartFile> inputFileList, List<String> deleteFileUrlList) {
        Long capacity = fileRepository.findCapacityByLectureId(lectureId);
        // 한 번도 파일을 등록한 적이 없을 경우에 대한 처리
        if(capacity == null) {
            capacity = 0L;
        }

        // 삭제될 파일들이 존재한다면, 해당 파일들의 용량을 조회해서 capacity에서 제거
        if(!deleteFileUrlList.isEmpty()) {
            Long deleteCapacity = fileRepository.findDeleteCapacityByUrlList(deleteFileUrlList);
            capacity -= deleteCapacity;
        }

        // 추가될 파일 각각의 용량 확인 및 전체 용량 제한을 위반하는지 확인
        for(MultipartFile multipartFile : inputFileList) { // 파일당 용량 검증 로직
            if(multipartFile.getSize() >= 5242880) {
                throw new BoardException(ErrorCode.FILE_SIZE_EXCEED);
            }
            capacity += multipartFile.getSize();
        }

        // 500MB 초과 여부 조건 반환
        return capacity > 524288000;
    }

    /**
     * Baord의 등록/수정이 일어났을 때 알림 전송
     * @param member - 게시글 작성자 정보
     * @param memberAndLecture - 게시글 작성자가 속해있는 클래스 관련 정보
     * @param board - 작성한 게시글 정보
     */
    private void sendBoardNotification(Member member, MemberAndLecture memberAndLecture, Board board) {
        // FCM 알림 전송 로직 도입
        String title = "새로운 게시글 등록!";
        String body = member.getName() + "님이 게시물을 등록했어요! 내용을 확인해보세요!";

        // 선생님&학생들에게 알림 전송하기 로직
        Notification teacherNotification = Notification.builder()
                .memberId(member.getMemberId())
                .title(title)
                .body(body)
                .name("teacherPost")
                .lectureId(memberAndLecture.getLecture().getLectureId())
                .boardId(board.getBoardId())
                .build();
        Notification studentNotification = Notification.builder()
                .memberId(member.getMemberId())
                .title(title)
                .body(body)
                .name("studentPost")
                .lectureId(memberAndLecture.getLecture().getLectureId())
                .boardId(board.getBoardId())
                .build();
        Notification saveStudentNotification = notificationRepository.save(studentNotification);

        // 선생님은 별도로 알림 전송 & 학생은 모아서 전송
        List<Member> memberList = memberRepository.findMemberListByLectureId(memberAndLecture.getLecture().getLectureId());
        List<MemberAndNotification> memberAndNotificationList = new ArrayList<>();
        List<String> tokenList = new ArrayList<>();
        if(!memberList.isEmpty()) {
            memberList.forEach(m -> {
                if(m.getType().equals(MemberType.TEACHER)) {
                    Notification saveTeacherNotification = notificationRepository.save(teacherNotification);
                    memberAndNotificationList.add(
                            MemberAndNotification.builder()
                                    .memberId(m.getMemberId())
                                    .notificationId(saveTeacherNotification.getNotificationId())
                                    .build()
                    );

                    // 선생님은 한 명이므로, 바로 전송될 수 있도록 함
                    if(m.getToken() != null) {
                        Message message = fcmClient.makeMessage(m.getToken(), saveTeacherNotification.getTitle(), saveTeacherNotification.getBody(), saveTeacherNotification.getName(), saveTeacherNotification.getLectureId(), saveTeacherNotification.getLessonId(), saveTeacherNotification.getDate(), saveTeacherNotification.getUrl(), saveTeacherNotification.getBoardId());
                        fcmClient.send(message);
                    }
                }
                else if(!m.getMemberId().equals(member.getMemberId()) && m.getType().equals(MemberType.STUDENT)){
                    if(m.getToken() != null) {
                        tokenList.add(m.getToken());
                    }
                    memberAndNotificationList.add(
                            MemberAndNotification.builder()
                                    .memberId(m.getMemberId())
                                    .notificationId(saveStudentNotification.getNotificationId())
                                    .build()
                    );
                }
            });
            memberAndNotificationJdbcRepository.saveAll(memberAndNotificationList);

            if (!tokenList.isEmpty()) {
                MulticastMessage multicastMessage = fcmClient.makeMulticastMessage(tokenList, saveStudentNotification);
                fcmClient.sendMulticast(tokenList, multicastMessage);
            }
        }
    }

    /**
     * Reply의 등록이 일어났을 때 알림 전송
     * @param board - 댓글이 작성된 게시글 정보
     * @param reply - 작성된 댓글 정보
     */
    private void sendReplyNotification(Member member, MemberAndLecture memberAndLecture, Board board, Reply reply) {
        // FCM 알림 전송 로직 도입
        Member writer = board.getMember();
        String title = "새로운 댓글 등록!";
        String body = member.getName() + "님이 댓글을 등록했어요! 내용을 확인해보세요!";

        Notification notification = Notification.builder()
                .memberId(member.getMemberId())
                .title(title)
                .body(body)
                .name((writer.getType().equals(MemberType.TEACHER)) ? "teacherPost" : "studentLessonReport")
                .lectureId(memberAndLecture.getLecture().getLectureId())
                .boardId(reply.getBoard().getBoardId())
                .build();
        Notification saveNotification = notificationRepository.save(notification);

        List<MemberAndNotification> memberAndNotificationList = new ArrayList<>();
        memberAndNotificationList.add(MemberAndNotification.builder()
                .memberId(writer.getMemberId())
                .notificationId(saveNotification.getNotificationId())
                .build());
        memberAndNotificationJdbcRepository.saveAll(memberAndNotificationList);

        if(writer.getToken() != null) {
            Message message = fcmClient.makeMessage(writer.getToken(), notification.getTitle(), notification.getBody(), notification.getName(), notification.getLectureId(), notification.getLessonId(), notification.getDate(), notification.getUrl(), notification.getBoardId());
            fcmClient.send(message);
        }
    }
}
