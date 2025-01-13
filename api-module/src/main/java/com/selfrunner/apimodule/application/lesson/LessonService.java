package com.selfrunner.apimodule.application.lesson;

import com.google.firebase.messaging.MulticastMessage;
import com.selfrunner.commonmodule.dto.homework.request.HomeworkReq;
import com.selfrunner.commonmodule.dto.homework.response.HomeworkRes;
import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.common.Schedule;
import com.selfrunner.domainmodule.domain.homework.Homework;
import com.selfrunner.domainmodule.domain.homework.repository.HomeworkJdbcRepository;
import com.selfrunner.domainmodule.domain.homework.repository.HomeworkRepository;
import com.selfrunner.commonmodule.dto.lesson.request.PatchLessonMetaRes;
import com.selfrunner.commonmodule.dto.lesson.request.PostLessonReq;
import com.selfrunner.commonmodule.dto.lesson.request.PutLessonIdReq;
import com.selfrunner.commonmodule.dto.lesson.request.PutLessonReq;
import com.selfrunner.commonmodule.dto.lesson.response.LessonIdRes;
import com.selfrunner.commonmodule.dto.lesson.response.LessonMetaRes;
import com.selfrunner.commonmodule.dto.lesson.response.LessonProgressRes;
import com.selfrunner.commonmodule.dto.lesson.response.LessonRes;
import com.selfrunner.domainmodule.domain.lesson.Lesson;
import com.selfrunner.commonmodule.vo.lesson.Participant;
import com.selfrunner.domainmodule.domain.lesson.repository.LessonRepository;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.domainmodule.domain.member.MemberAndLecture;
import com.selfrunner.domainmodule.domain.member.MemberAndNotification;
import com.selfrunner.commonmodule.enumerate.member.MemberType;
import com.selfrunner.domainmodule.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.domainmodule.domain.member.repository.MemberAndNotificationJdbcRepository;
import com.selfrunner.domainmodule.domain.member.repository.MemberRepository;
import com.selfrunner.commonmodule.vo.member.MemberMeta;
import com.selfrunner.domainmodule.domain.notification.Notification;
import com.selfrunner.domainmodule.domain.notification.repository.NotificationRepository;
import com.selfrunner.notificationmodule.fcm.FCMClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LessonService {

    private final LessonRepository lessonRepository;
    private final MemberAndLectureRepository memberAndLectureRepository;
    private final HomeworkRepository homeworkRepository;
    private final HomeworkJdbcRepository homeworkJdbcRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final MemberAndNotificationJdbcRepository memberAndNotificationJdbcRepository;
    private final FCMClient fcmClient;

    @Transactional
    public LessonIdRes register(Member member, PostLessonReq postLessonReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, postLessonReq.getLectureId())
                .orElseThrow(() -> new ApplicationException((ErrorCode.UNAUTHORIZED_EXCEPTION)));
        if(memberAndLecture.getIsTeacher().equals(Boolean.FALSE)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        // 정규 수업 정보 등록
        Lesson lesson = Lesson.builder()
                .lecture(memberAndLecture.getLecture())
                .type(postLessonReq.getType())
                .participants(postLessonReq.getParticipants())
                .feedback(postLessonReq.getFeedback())
                .progresses(postLessonReq.getProgresses())
                .date(postLessonReq.getDate())
                .time(Schedule.convertToSchedule(postLessonReq.getTime()))
                .build();
        Lesson saveLesson = lessonRepository.save(lesson);
        List<Homework> homeworkList = new ArrayList<>();
        List<Long> studentIdList = new ArrayList<>();
        for (Participant participant : postLessonReq.getParticipants()) {
            // 숙제 리스트 생성
            List<Homework> tempHomeworkList = postLessonReq.getHomeworks().stream()
                    .map(homeworkReq -> Homework.builder()
                            .lessonId(lesson.getLessonId())
                            .memberId(participant.getMemberId())
                            .body(homeworkReq.getBody())
                            .deadline(homeworkReq.getDeadline())
                            .isFinish(homeworkReq.getIsFinish())
                            .build())
                    .collect(Collectors.toList());
            homeworkList.addAll(tempHomeworkList);

            if(!participant.getMemberId().equals(member.getMemberId())) {
                studentIdList.add(participant.getMemberId());
            }
        }
        homeworkJdbcRepository.saveAll(homeworkList);

        // FCM 송신 TODO: 비동기 처리를 통한 성능 향상
        String title = "새로운 수업 등록!";
        String body = member.getName() + " 선생님이 수업을 등록했어요! 숙제를 확인해보세요!";
        Notification notification = Notification.builder()
                .memberId(member.getMemberId())
                .title(title)
                .body(body)
                .name("studentLessonReport")
                .lectureId(postLessonReq.getLectureId())
                .lessonId(saveLesson.getLessonId())
                .build();
        Notification saveNotification = notificationRepository.save(notification);
        List<MemberAndNotification> memberAndNotificationList = studentIdList.stream()
                .map(studentId -> MemberAndNotification.builder()
                        .memberId(studentId)
                        .notificationId(saveNotification.getNotificationId())
                        .build())
                .collect(Collectors.toList());
        memberAndNotificationJdbcRepository.saveAll(memberAndNotificationList);
        List<String> tokenList = memberRepository.findTokenListByMemberIdList(studentIdList);
        if(!tokenList.isEmpty()) {
            MulticastMessage multicastMessage =
                    fcmClient.makeMulticastMessage(tokenList,
                            saveNotification.getTitle(),
                            saveNotification.getBody(),
                            saveNotification.getName(),
                            saveNotification.getLectureId(),
                            saveNotification.getLessonId(),
                            saveNotification.getDate(),
                            saveNotification.getUrl(),
                            saveNotification.getBoardId());
            fcmClient.sendMulticast(tokenList, multicastMessage);
        }

        // Response
        return new LessonIdRes(saveLesson.getLessonId());
    }

    @Transactional
    public LessonRes update(Member member, Long lessonId, PutLessonReq putLessonReq) {
        // Validation
        if(!member.getType().equals(MemberType.TEACHER)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_LESSON));
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic: Homework 변경 여부 확인 진행
        // lesson은 무조건 업데이트 + 변경사항이 발생하면 숙제 업데이트 진행
        if((changeParticipant(lesson, putLessonReq) || changeHomework(member, lesson, putLessonReq))) {
            homeworkRepository.deleteHomeworkByLessonId(lessonId);
            if(putLessonReq.getHomeworks() != null) {
                List<Homework> homeworkInsertList = new ArrayList<>();
                for (Participant participant : putLessonReq.getParticipants()) {
                    List<Homework> tempHomeworkList = putLessonReq.getHomeworks().stream()
                            .map(homeworkReq -> Homework.builder()
                                    .lessonId(lesson.getLessonId())
                                    .memberId(participant.getMemberId())
                                    .body(homeworkReq.getBody())
                                    .deadline(homeworkReq.getDeadline())
                                    .isFinish(homeworkReq.getIsFinish())
                                    .build())
                            .collect(Collectors.toList());
                    homeworkInsertList.addAll(tempHomeworkList);
                }
                homeworkJdbcRepository.saveAll(homeworkInsertList);
            }
        }
        lesson.update(putLessonReq);

        List<HomeworkRes> homeworkRes = homeworkRepository.findAllByMemberIdAndLessonId(member.getMemberId(), lessonId);
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lesson.getLecture().getLectureId()).orElse(null);

        // FCM 송신 TODO: 비동기 처리를 통한 성능 향상
        String title = "수업 업데이트!";
        String body = member.getName() + " 선생님이 " + lesson.getDate().format(DateTimeFormatter.ofPattern("M월 d일")) +  " 수업을 업데이트했어요!" + "\n" + "접속해서 업데이트 내용을 확인해보세요!";
        Notification notification = Notification.builder()
                .memberId(member.getMemberId())
                .title(title)
                .body(body)
                .name("studentLessonReport")
                .lectureId(memberAndLecture.getLecture().getLectureId())
                .lessonId(lessonId)
                .build();
        Notification saveNotification = notificationRepository.save(notification);
        List<Long> studentIdList = lesson.getParticipants().stream()
                .filter(participant -> !participant.getMemberId().equals(member.getMemberId()))
                .map(Participant::getMemberId).collect(Collectors.toList());
        List<MemberAndNotification> memberAndNotificationList = studentIdList.stream()
                .map(studentId -> MemberAndNotification.builder()
                        .memberId(studentId)
                        .notificationId(saveNotification.getNotificationId())
                        .build())
                .collect(Collectors.toList());
        memberAndNotificationJdbcRepository.saveAll(memberAndNotificationList);
        List<String> tokenList = memberRepository.findTokenListByMemberIdList(studentIdList);
        if(!tokenList.isEmpty()) {
            MulticastMessage multicastMessage = fcmClient.makeMulticastMessage(tokenList,
                    saveNotification.getTitle(),
                    saveNotification.getBody(),
                    saveNotification.getName(),
                    saveNotification.getLectureId(),
                    saveNotification.getLessonId(),
                    saveNotification.getDate(),
                    saveNotification.getUrl(),
                    saveNotification.getBoardId());
            fcmClient.sendMulticast(tokenList, multicastMessage);
        }

        // Response
        return LessonRes.toDto(lesson.getLessonId(),
                lesson.getLecture().getLectureId(),
                memberAndLecture.getColor(),
                lesson.getType(),
                lesson.getParticipants(),
                lesson.getFeedback(),
                lesson.getProgresses(),
                homeworkRes,
                lesson.getDate(),
                lesson.getWeekday(),
                lesson.getStartTime(),
                lesson.getEndTime(),
                memberMetas,
                Boolean.TRUE);
    }

    @Transactional
    public LessonMetaRes updateMeta(Member member, Long lessonId, PatchLessonMetaRes patchLessonMetaRes) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_LESSON));
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic: 수업 정보 / 숙제 정보 업데이트
        // 수업 여부에 따라서 homework에 Participant 업데이트 진행 필요
        List<HomeworkRes> homeworkResList = homeworkRepository.findAllByMemberIdAndLessonId(member.getMemberId(), lessonId);
        if(!homeworkResList.isEmpty()) {
            List<Homework> homeworkList = new ArrayList<>();

            if(lesson.getParticipants() != null) {
                List<Long> deleteIdList = lesson.getParticipants().stream()
                        .filter(participant -> patchLessonMetaRes.getParticipants().stream().noneMatch(patchParticipant -> Objects.equals(participant.getMemberId(), patchParticipant.getMemberId())))
                        .map(Participant::getMemberId)
                        .collect(Collectors.toList());
                homeworkRepository.deleteAllByLessonIdAndMemberIdList(lessonId, deleteIdList);

                for (Participant participant : patchLessonMetaRes.getParticipants()) {
                    if(lesson.getParticipants().stream().noneMatch(lessonParticipant -> Objects.equals(lessonParticipant.getMemberId(), participant.getMemberId()))) {
                        List<Homework> tempHomeworkList = homeworkResList.stream()
                                .map(homeworkRes -> {
                                    // homeworkRes에서 필요한 내용을 가져와서 Homework.builder()를 사용하여 Homework 객체를 생성
                                    String body = homeworkRes.getBody();
                                    LocalDate deadLine = homeworkRes.getDeadline();
                                    // homeworkRes 내의 값을 사용하여 Homework 객체 생성
                                    Homework homework = Homework.builder()
                                            .lessonId(lessonId)
                                            .memberId(participant.getMemberId())
                                            .body(body)
                                            .deadline(deadLine)
                                            .isFinish(Boolean.FALSE)
                                            .build();

                                    return homework;
                                })
                                .collect(Collectors.toList());
                        homeworkList.addAll(tempHomeworkList);
                    }
                }
            }

            if(lesson.getParticipants() == null) {
                for (Participant participant : patchLessonMetaRes.getParticipants()) {
                    List<Homework> tempHomeworkList = homeworkResList.stream()
                            .map(homeworkRes -> {
                                // homeworkRes에서 필요한 내용을 가져와서 Homework.builder()를 사용하여 Homework 객체를 생성
                                String body = homeworkRes.getBody();
                                LocalDate deadLine = homeworkRes.getDeadline();
                                // homeworkRes 내의 값을 사용하여 Homework 객체 생성
                                Homework homework = Homework.builder()
                                        .lessonId(lessonId)
                                        .memberId(participant.getMemberId())
                                        .body(body)
                                        .deadline(deadLine)
                                        .isFinish(Boolean.FALSE)
                                        .build();

                                return homework;
                            })
                            .collect(Collectors.toList());
                    homeworkList.addAll(tempHomeworkList);
                }
            }

            homeworkJdbcRepository.saveAll(homeworkList);
        }

        // 수업 정보는 무조건 업데이트 진행되므로 조건 필요 X
        lesson.updateMeta(patchLessonMetaRes);

        // FCM 송신 TODO: 비동기 처리를 통한 성능 향상
        String title = "수업 업데이트!";
        String body = member.getName() + " 선생님이 " + lesson.getDate().format(DateTimeFormatter.ofPattern("M월 d일")) +  " 수업을 업데이트했어요!" + "\n" + "접속해서 업데이트 내용을 확인해보세요!";
        Notification notification = Notification.builder()
                .memberId(member.getMemberId())
                .title(title)
                .body(body)
                .name("studentLessonReport")
                .lectureId(memberAndLecture.getLecture().getLectureId())
                .lessonId(lessonId)
                .build();
        Notification saveNotification = notificationRepository.save(notification);
        List<Long> studentIdList = patchLessonMetaRes.getParticipants().stream()
                .map(Participant::getMemberId)
                .filter(memberId -> !memberId.equals(member.getMemberId()))
                .collect(Collectors.toList());
        List<MemberAndNotification> memberAndNotificationList = studentIdList.stream()
                .map(studentId -> MemberAndNotification.builder()
                        .memberId(studentId)
                        .notificationId(saveNotification.getNotificationId())
                        .build())
                .collect(Collectors.toList());
        memberAndNotificationJdbcRepository.saveAll(memberAndNotificationList);
        List<String> tokenList = memberRepository.findTokenListByMemberIdList(studentIdList);
        if(!tokenList.isEmpty()) {
            MulticastMessage multicastMessage =
                    fcmClient.makeMulticastMessage(tokenList,
                            saveNotification.getTitle(),
                            saveNotification.getBody(),
                            saveNotification.getName(),
                            saveNotification.getLectureId(),
                            saveNotification.getLessonId(),
                            saveNotification.getDate(),
                            saveNotification.getUrl(),
                            saveNotification.getBoardId());
            fcmClient.sendMulticast(tokenList, multicastMessage);
        }

        // Response
        return new LessonMetaRes(lesson.getLessonId(),
                lesson.getLecture().getLectureId(),
                lesson.getType(), lesson.getDate(),
                Schedule.convertToScheduleVo(new Schedule(lesson.getWeekday(), lesson.getStartTime(), lesson.getEndTime())),
                lesson.getParticipants());
    }

    @Transactional
    public void deleteAll(Member member, Long lectureId, List<PutLessonIdReq> putLessonIdReqList) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<Long> lessonIdList = putLessonIdReqList.stream()
                .map(putLessonIdReq -> putLessonIdReq.getLessonId())
                .collect(Collectors.toList());
        List<Lesson> lessonList = lessonRepository.findAllById(lessonIdList);
        lessonRepository.deleteAll(lessonList);

        // Response
    }

    @Transactional
    public void delete(Member member, Long lessonId) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_LESSON));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        homeworkRepository.deleteAllByLessonId(lessonId);
        lessonRepository.delete(lesson);

        // Response
    }

    public LessonRes get(Member member, Long lessonId) {
        // Validation
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ApplicationException((ErrorCode.NOT_EXIST_LESSON)));
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId())
                .orElseThrow(() -> new ApplicationException((ErrorCode.UNAUTHORIZED_EXCEPTION)));

        // Business Logic
        List<HomeworkRes> homeworkRes = homeworkRepository.findAllByMemberIdAndLessonId(member.getMemberId(), lessonId);
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lesson.getLecture().getLectureId())
                .orElseThrow(() -> new ApplicationException((ErrorCode.NOT_FOUND_EXCEPTION)));

        // Response
        return LessonRes.toDto(lesson.getLessonId(),
                lesson.getLecture().getLectureId(),
                lesson.getLecture().getColor(),
                lesson.getType(),
                lesson.getParticipants(),
                lesson.getFeedback(),
                lesson.getProgresses(),
                homeworkRes,
                lesson.getDate(),
                lesson.getWeekday(),
                lesson.getStartTime(),
                lesson.getEndTime(),memberMetas,
                (lesson.getCreatedAt().equals(lesson.getModifiedAt())) ? Boolean.TRUE : Boolean.FALSE);
    }

    public List<LessonMetaRes> getAllLessonMeta(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException((ErrorCode.UNAUTHORIZED_EXCEPTION)));

        // Business Logic
        List<LessonMetaRes> lessonMetaRes = lessonRepository.findAllLessonMetaByLectureId(lectureId)
                .orElseThrow(() -> new ApplicationException((ErrorCode.NOT_EXIST_LESSON)));
        // 오름차순 정렬
        Collections.sort(lessonMetaRes);

        // Response
        return lessonMetaRes;
    }

    public List<LessonMetaRes> getAllLessonMetaByYearMonth(Member member, String year, String month) {
        // Validation

        // Business Logic
        List<Long> lectureIdList = memberAndLectureRepository.findLectureIdByMember(member)
                .orElseThrow(() -> new ApplicationException((ErrorCode.NOT_FOUND_EXCEPTION)));
        List<LessonMetaRes> lessonMetaRes = lessonRepository.findAllLessonMetaByYearMonth(lectureIdList, year, month).orElse(null);
        // 오름차순 정렬
        Collections.sort(lessonMetaRes);

        // Response
        return lessonMetaRes;
    }

    public List<LessonProgressRes> getAllProgress(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException((ErrorCode.UNAUTHORIZED_EXCEPTION)));

        // Business Logic && Response
        return lessonRepository.findAllProgressByLectureId(lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
    }

    /**
     * 참여자 변동사항 확인
     * @param lesson
     * @param putLessonReq
     * @return
     */
    private boolean changeParticipant(Lesson lesson, PutLessonReq putLessonReq) {
        boolean needUpdate = Boolean.FALSE;
        if(lesson.getParticipants() == null) {
            needUpdate = Boolean.TRUE;
        }
        if(lesson.getParticipants() != null) {
            if(lesson.getParticipants().size() != putLessonReq.getParticipants().size()) {
                needUpdate = Boolean.TRUE;
            }
            if(lesson.getParticipants().size() == putLessonReq.getParticipants().size()) {
                for(Participant lessonParticipant : lesson.getParticipants()) {
                    System.out.println(lessonParticipant.getMemberId());
                    boolean temp = Boolean.FALSE;
                    for(Participant dtoParticipant : putLessonReq.getParticipants()) {
                        if(lessonParticipant.getMemberId().equals(dtoParticipant.getMemberId())) {
                            temp = Boolean.TRUE;
                        }
                    }
                    if(!temp) {
                        needUpdate = Boolean.TRUE;
                        break;
                    }
                }
            }
        }

        return needUpdate;
    }

    /**
     * 숙제 변동사항 확인
     * @param member
     * @param lesson
     * @param putLessonReq
     * @return
     */
    private boolean changeHomework(Member member, Lesson lesson, PutLessonReq putLessonReq) {
        boolean needUpdate = Boolean.FALSE;
        List<Homework> homeworkRowList = homeworkRepository.findAllByMemberIdAndLessonIdAndDeletedAtIsNull(member.getMemberId(), lesson.getLessonId()).orElse(null);
        if(homeworkRowList != null && putLessonReq.getHomeworks() == null ) {
            needUpdate = Boolean.TRUE;
        }
        if(homeworkRowList == null && putLessonReq.getHomeworks() != null ) {
            needUpdate = Boolean.TRUE;
        }
        if(homeworkRowList != null && putLessonReq.getHomeworks() != null) {
            if(homeworkRowList.size() == putLessonReq.getHomeworks().size()) {
                for(Homework homework : homeworkRowList) {
                    for(HomeworkReq homeworkReq : putLessonReq.getHomeworks()) {
                        if(!homework.isSameHomework(homeworkReq)) {
                            needUpdate = Boolean.TRUE;
                            break;
                        }
                    }
                    if(needUpdate) {
                        break;
                    }
                }
            }
            if(homeworkRowList.size() != putLessonReq.getHomeworks().size()) {
                needUpdate = Boolean.TRUE;
            }
        }

        return needUpdate;
    }
}
