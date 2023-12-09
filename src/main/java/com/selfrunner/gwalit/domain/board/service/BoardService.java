package com.selfrunner.gwalit.domain.board.service;

import com.selfrunner.gwalit.domain.board.dto.request.PostBoardReq;
import com.selfrunner.gwalit.domain.board.dto.request.PutBoardReq;
import com.selfrunner.gwalit.domain.board.dto.request.ReplyReq;
import com.selfrunner.gwalit.domain.board.dto.response.BoardMetaRes;
import com.selfrunner.gwalit.domain.board.dto.response.BoardReplyRes;
import com.selfrunner.gwalit.domain.board.dto.response.BoardRes;
import com.selfrunner.gwalit.domain.board.dto.response.ReplyRes;
import com.selfrunner.gwalit.domain.board.entity.Board;
import com.selfrunner.gwalit.domain.board.entity.File;
import com.selfrunner.gwalit.domain.board.exception.BoardException;
import com.selfrunner.gwalit.domain.board.repository.BoardRepository;
import com.selfrunner.gwalit.domain.board.repository.FileJdbcRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.aws.S3Client;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileJdbcRepository fileJdbcRepository;
    private final MemberAndLectureRepository memberAndLectureRepository;
    private final S3Client s3Client;

    public BoardRes registerBoard(Member member, List<MultipartFile> multipartFileList, PostBoardReq postBoardReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, postBoardReq.getLectureId()).orElseThrow(() -> new BoardException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        // TODO: 클래스당 용량 검사 로직 추가 필요
        for(MultipartFile multipartFile : multipartFileList) { // 파일당 용량 검증 로직
            if(multipartFile.getSize() >= 5242880) {
                throw new BoardException(ErrorCode.FILE_SIZE_EXCEED);
            }
        }

        // Business Logic
        Board board = postBoardReq.toEntity(member.getMemberId());
        Board saveBoard = boardRepository.save(board);
        // File이 존재하면 S3 업로드 진행
        List<String> fileUrlList = (!multipartFileList.isEmpty()) ? uploadFileList(multipartFileList, member.getMemberId(), memberAndLecture.getLecture().getLectureId(), saveBoard.getBoardId(), null): null;

        // Response
        return new BoardRes(saveBoard, fileUrlList);
    }

    public BoardRes updateBoard(Member member, Long boardId, List<MultipartFile> multipartFileList, PutBoardReq putBoardReq) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public void deleteBoard() {
        // Validation

        // Business Logic

        // Response
    }

    public BoardRes changeQuestionStatus(Member member, Long boardId) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public BoardReplyRes getOneBoard(@Auth Member member, Long boardId) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public Slice<BoardMetaRes> getBoardPagination(Member member, String category, Long cursor, Pageable pageable) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public List<BoardRes> getOpenQuestion(Member member) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public ReplyRes registerReply(Member member, Long boardId, List<MultipartFile> multipartFileList, ReplyReq replyReq) {
        // Validation
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic

        // Response
        return null;
    }

    public void deleteReply(Member member, Long boardId, Long replyId) {
        // Validation

        // Business Logic

        // Response
    }

    public Slice<ReplyRes> getReplyPagination(Member member, Long boardId, Long cursor, Pageable pageable) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    private List<String> uploadFileList(List<MultipartFile> multipartFileList, Long memberId, Long lectureId, Long boardId, Long replyId) {
        List<String> fileUrlList = new ArrayList<>();
        List<File> fileList = new ArrayList<>();
        multipartFileList.forEach(
                multipartFile -> {
                    try {
                        String url = s3Client.upload(multipartFile, "board/" + lectureId);
                        fileUrlList.add(url);
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
                    } catch (Exception e) {
                        throw new BoardException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
                    }
                }
        );
        fileJdbcRepository.saveAll(fileList);

        return fileUrlList;
    }
}
