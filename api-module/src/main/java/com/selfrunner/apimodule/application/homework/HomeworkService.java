package com.selfrunner.apimodule.application.homework;

import com.google.firebase.messaging.Message;
import com.selfrunner.commonmodule.dto.homework.request.HomeworkRemindReq;
import com.selfrunner.commonmodule.dto.homework.request.HomeworkReq;
import com.selfrunner.commonmodule.dto.homework.response.HomeworkMainRes;
import com.selfrunner.commonmodule.dto.homework.response.HomeworkRes;
import com.selfrunner.commonmodule.dto.homework.response.HomeworkStatisticsRes;
import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.commonmodule.vo.homework.HomeworkRemind;
import com.selfrunner.domainmodule.domain.homework.Homework;
import com.selfrunner.domainmodule.domain.homework.repository.HomeworkRepository;
import com.selfrunner.domainmodule.domain.lesson.Lesson;
import com.selfrunner.domainmodule.domain.lesson.repository.LessonRepository;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.domainmodule.domain.member.MemberAndNotification;
import com.selfrunner.commonmodule.enumerate.member.MemberType;
import com.selfrunner.domainmodule.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.domainmodule.domain.member.repository.MemberAndNotificationJdbcRepository;
import com.selfrunner.domainmodule.domain.notification.Notification;
import com.selfrunner.domainmodule.domain.notification.repository.NotificationRepository;
import com.selfrunner.notificationmodule.fcm.FCMClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final LessonRepository lessonRepository;
    private final MemberAndLectureRepository memberAndLectureRepository;
    private final NotificationRepository notificationRepository;
    private final MemberAndNotificationJdbcRepository memberAndNotificationJdbcRepository;
    private final FCMClient fcmClient;

    @Transactional
    public HomeworkRes register(Member member, Long lessonId, HomeworkReq homeworkReq) {
        // Validation
        if(lessonId != null) {
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_LESSON)); // 해당 수업이 미존재 시, 에러 반환
            memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId())
                    .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)); // 해당 수업과 관련된 클래스에 권한 없는 경우 에러 반환
        }

        // Business Logic
        Homework homework =  Homework.builder()
                .lessonId(lessonId)
                .memberId(member.getMemberId())
                .body(homeworkReq.getBody())
                .deadline(homeworkReq.getDeadline())
                .isFinish(homeworkReq.getIsFinish())
                .build();
        Homework saveHomework = homeworkRepository.save(homework);

        // Response
        return new HomeworkRes(saveHomework.getHomeworkId(),
                saveHomework.getLessonId(),
                saveHomework.getMemberId(),
                saveHomework.getBody(),
                saveHomework.getDeadline(),
                saveHomework.getIsFinish());
    }

    @Transactional
    public HomeworkMainRes update(Member member, Long homeworkId, HomeworkReq homeworkReq) {
        // Validation
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(!homework.getMemberId().equals(member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        homework.update(homeworkReq);

        // Response
        return homeworkRepository.findHomeworkByHomeworkId(member, homeworkId);
    }

    @Transactional
    public void delete(Member member, Long homeworkId) {
        // Validation
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        homeworkRepository.delete(homework);

        // Response
    }

    public HomeworkRes get(Member member, Long homeworkId) {
        // Validation
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(!homework.getMemberId().equals(member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic && Response
        return new HomeworkRes(homework.getHomeworkId(),
                homework.getLessonId(),
                homework.getMemberId(),
                homework.getBody(),
                homework.getDeadline(),
                homework.getIsFinish());
    }

    public List<HomeworkRes> getAll(Member member) {
        // Validation
        if(member.getType().equals(MemberType.TEACHER)) { // 학생용 API 검증
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        List<Homework> homeworkList = homeworkRepository.findAllByMemberId(member.getMemberId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return homeworkList.stream()
                .map(homework -> new HomeworkRes(homework.getHomeworkId(),
                        homework.getLessonId(),
                        homework.getMemberId(),
                        homework.getBody(),
                        homework.getDeadline(),
                        homework.getIsFinish()))
                .collect(Collectors.toList());
    }

    public List<HomeworkMainRes> getMain(Member member) {
        // Validation

        // Business Logic
        List<Long> lectureIdList = memberAndLectureRepository.findLectureIdByMember(member).orElse(null);
        List<Long> lessonIdList = lessonRepository.findRecentLessonIdByLectureIdList(lectureIdList).orElse(null);

        // Response
        return homeworkRepository.findRecentHomeworkByMemberAndLessonIdList(member, lessonIdList)
                .orElse(null);
    }

    public List<HomeworkMainRes> getLecture(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<Long> lessonIdList = new ArrayList<>();
        lessonRepository.findRecentLessonIdByLectureId(lectureId).ifPresent(lessonIdList::add);

        // Response
        return homeworkRepository.findRecentHomeworkByMemberAndLessonIdList(member, lessonIdList).orElse(null);
    }

    public List<HomeworkMainRes> getList(Member member, Long lectureId, String type) {
        // Validation - 학생용 API
        if(member.getType().equals(MemberType.TEACHER)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic - all: 전체 리스트 / finished: 완료 리스트 / unfinished: 미완료 리스트
        List<HomeworkMainRes> homeworkMainResList = new ArrayList<>();
        if(lectureId == null) {
            if(type.equals("all")) {
                homeworkMainResList = homeworkRepository.findAllHomeworkByMember(member).orElse(null);
            }
            if(type.equals("finished")) {
                homeworkMainResList = homeworkRepository.findAllHomeworkByMemberAndType(member, Boolean.TRUE)
                        .orElse(null);
            }
            if(type.equals("unfinished")) {
                homeworkMainResList = homeworkRepository.findAllHomeworkByMemberAndType(member, Boolean.FALSE)
                        .orElse(null);
            }
        }
        if(lectureId != null) {
            if(type.equals("all")) {
                homeworkMainResList = homeworkRepository.findAllHomeworkByMemberAndLectureId(member, lectureId)
                        .orElse(null);
            }
            if(type.equals("finished")) {
                homeworkMainResList = homeworkRepository.findAllHomeworkByMemberAndLectureIdAndType(member, lectureId, Boolean.TRUE)
                        .orElse(null);
            }
            if(type.equals("unfinished")) {
                homeworkMainResList = homeworkRepository.findAllHomeworkByMemberAndLectureIdAndType(member, lectureId, Boolean.FALSE)
                        .orElse(null);
            }
        }

        // Response
        return homeworkMainResList;
    }

    public List<HomeworkStatisticsRes> getStatisticsList(Member member, Long homeworkId) {
        // Validation
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(!homework.getMemberId().equals(member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic && Response
        return homeworkRepository.findAllByBodyAndCreatedAt(member.getMemberId(), homework.getLessonId(), homework.getBody(), homework.getDeadline(), homework.getCreatedAt());
    }

    @Transactional
    public void sendHomeworkRemindNotification(Member member, List<HomeworkRemindReq> homeworkRemindReqList) {
        // Validation

        // Business Logic
        List<Long> homeworkIdList = homeworkRemindReqList.stream()
                .map(HomeworkRemindReq::getHomeworkId)
                .collect(Collectors.toList());
        List<HomeworkRemind> homeworkRemindList = homeworkRepository.findHomeworkByIsFinish(homeworkIdList);
        if(!homeworkRemindList.isEmpty()) {
            List<String> tokenList = new ArrayList<>();
            List<Notification> notificationList = homeworkRemindList.stream()
                    .map(homeworkRemind -> {
                        tokenList.add(homeworkRemind.getToken());
                        return Notification.builder()
                                .memberId(member.getMemberId())
                                .title(homeworkRemind.getLectureName() + " 숙제 리마인드!")
                                .body(homeworkRemind.getBody())
                                .name("studentLessonReport")
                                .lectureId(homeworkRemind.getLectureId())
                                .lessonId(homeworkRemind.getLessonId())
                                .build();
                    })
                    .collect(Collectors.toList());
            List<Notification> saveNotification = notificationRepository.saveAll(notificationList);
            List<MemberAndNotification> memberAndNotificationList = new ArrayList<>();
            for(int i = 0; i < notificationList.size(); i++) {
                memberAndNotificationList.add(
                        MemberAndNotification.builder()
                                .memberId(homeworkRemindList.get(i).getMemberId())
                                .notificationId(saveNotification.get(i).getNotificationId())
                                .build()
                );
                Message message = fcmClient.makeMessage(tokenList.get(i), notificationList.get(i).getTitle(), notificationList.get(i).getBody(), notificationList.get(i).getName(), notificationList.get(i).getLectureId(), notificationList.get(i).getLessonId(), notificationList.get(i).getDate(), notificationList.get(i).getUrl(), null);
                fcmClient.send(message);
            }
            memberAndNotificationJdbcRepository.saveAll(memberAndNotificationList);
        }

        // Response
    }
}
