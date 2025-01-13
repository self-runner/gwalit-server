package com.selfrunner.apimodule.application.lecture;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.Message;
import com.selfrunner.commonmodule.common.Day;
import com.selfrunner.commonmodule.dto.lecture.request.*;
import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.commonmodule.vo.lecture.ScheduleVo;
import com.selfrunner.domainmodule.common.Schedule;
import com.selfrunner.domainmodule.domain.homework.repository.HomeworkRepository;
import com.selfrunner.domainmodule.domain.lecture.Rule;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.domainmodule.domain.member.MemberAndLecture;
import com.selfrunner.commonmodule.dto.lecture.response.GetLectureMainRes;
import com.selfrunner.commonmodule.dto.lecture.response.GetLectureMetaRes;
import com.selfrunner.commonmodule.dto.lecture.response.GetLectureRes;
import com.selfrunner.commonmodule.dto.lecture.response.GetStudentRes;
import com.selfrunner.domainmodule.domain.lecture.Lecture;
import com.selfrunner.domainmodule.domain.lecture.repository.LectureRepository;
import com.selfrunner.commonmodule.dto.lesson.response.LessonMetaRes;
import com.selfrunner.domainmodule.domain.lesson.Lesson;
import com.selfrunner.domainmodule.domain.lesson.repository.LessonJdbcRepository;
import com.selfrunner.domainmodule.domain.lesson.repository.LessonRepository;
import com.selfrunner.commonmodule.enumerate.member.MemberState;
import com.selfrunner.commonmodule.enumerate.member.MemberType;
import com.selfrunner.domainmodule.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.domainmodule.domain.member.repository.MemberRepository;
import com.selfrunner.commonmodule.vo.member.MemberMeta;
import com.selfrunner.domainmodule.domain.task.repository.TaskRepository;
import com.selfrunner.notificationmodule.fcm.FCMClient;
import com.selfrunner.notificationmodule.sms.CoolSMSClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MemberAndLectureRepository memberAndLectureRepository;
    private final MemberRepository memberRepository;
    private final TaskRepository taskRepository;
    private final LessonRepository lessonRepository;
    private final LessonJdbcRepository lessonJdbcRepository;
    private final HomeworkRepository homeworkRepository;
    private final CoolSMSClient smsClient;
    private final FCMClient fcmClient;

    @Transactional
    public GetLectureMetaRes register(Member member, PostLectureReq postLectureReq) {
        // Valid
        if(member.getType() != MemberType.TEACHER) { // 방 생성 권한 없음
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        Long count = memberAndLectureRepository.findCountByMember(member);
        if(count > 15) { // Class 생성 제한 개수 16개
            throw new ApplicationException(ErrorCode.FAILED_MAKE_CLASS);
        }
        if(postLectureReq.getEndDate().isAfter(postLectureReq.getStartDate().plusYears(1).minusDays(1))) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
        if(postLectureReq.getSchedules().size() > 20) {
            throw new ApplicationException(ErrorCode.TOO_MANY_SCHEDULE);
        }

        // Business Logic
        Lecture lecture = Lecture.builder()
                .name(postLectureReq.getName())
                .color(postLectureReq.getColor())
                .subject(postLectureReq.getSubject())
                .subjectDetail(postLectureReq.getSubjectDetail())
                .startDate(postLectureReq.getStartDate())
                .endDate(postLectureReq.getEndDate())
                .rules(postLectureReq.getRules().stream()
                        .map(Rule::convertToRule)
                        .collect(Collectors.toList()))
                .schedules(postLectureReq.getSchedules().stream()
                        .map(Schedule::convertToSchedule)
                        .collect(Collectors.toList()))
                .build();
        Lecture saveLecture = lectureRepository.save(lecture);
        MemberAndLecture memberAndLecture = MemberAndLecture.builder()
                        .member(member)
                        .lecture(saveLecture)
                        .build();
        memberAndLectureRepository.save(memberAndLecture);
        List<Lesson> lessonList = new ArrayList<>();
        for(LocalDate now = postLectureReq.getStartDate(); now.isBefore(postLectureReq.getEndDate()); now = now.plusDays(1L)) {
            for(ScheduleVo scheduleVo : postLectureReq.getSchedules()) {
                if(now.getDayOfWeek().equals(getDayOfWeek(scheduleVo.getWeekday()))) {
                    Schedule schedule = Schedule.convertToSchedule(scheduleVo);
                    Lesson temp = new Lesson(saveLecture, "Regular", null, null, null, now, schedule);
                    lessonList.add(temp);
                }
            }
        }
        lessonJdbcRepository.saveAll(lessonList);

        // Response
        List<MemberMeta> memberMetas = new ArrayList<>();
        memberMetas.add(new MemberMeta(memberAndLecture.getMember().getMemberId(), member.getName(), memberAndLecture.getIsTeacher()));
        List<ScheduleVo> scheduleVoList = saveLecture.getSchedules().stream()
                .map(Schedule::convertToScheduleVo)
                .collect(Collectors.toList());
        return new GetLectureMetaRes(saveLecture.getLectureId(),
                memberAndLecture.getName(),
                memberAndLecture.getColor(),
                saveLecture.getSubject(),
                saveLecture.getSubjectDetail(),
                saveLecture.getStartDate(),
                saveLecture.getEndDate(),
                scheduleVoList,
                memberMetas);
    }

    @Transactional
    public void delete(Member member, Long lectureId) {
        // Validation
        if(member.getType() != MemberType.TEACHER) { // 방 삭제 권한 없음
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인

        // Business Logic
        memberAndLectureRepository.deleteMemberAndLectureByLectureId(lectureId);
        taskRepository.deleteAllByLectureLectureId(lectureId);
        List<Long> lessonIdList = lessonRepository.findAllLessonIdByLectureId(lectureId);
        homeworkRepository.deleteAllByLessonIdList(lessonIdList);
        lessonRepository.deleteAllByLectureLectureId(lectureId);
        lectureRepository.delete(memberAndLecture.getLecture());

        // Response
    }

    public GetLectureMetaRes get(Member member, Long lectureId) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인

        // Business Logic
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS));
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return new GetLectureMetaRes(lecture.getLectureId(),
                memberAndLecture.getName(),
                memberAndLecture.getColor(),
                lecture.getSubject(),
                lecture.getSubjectDetail(),
                lecture.getStartDate(),
                lecture.getEndDate(),
                lecture.getSchedules().stream()
                        .map(Schedule::convertToScheduleVo)
                        .collect(Collectors.toList()),
                memberMetas);
    }

    @Transactional
    public GetLectureMetaRes update(Member member, Long lectureId, PutLectureReq putLectureReq) {
        // Validation
        if(member.getType() != MemberType.TEACHER) { // 방 생성 권한 없음
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인
        if(ChronoUnit.DAYS.between(putLectureReq.getStartDate(), putLectureReq.getEndDate()) > 365) {
            throw new ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION);
        }
        if(putLectureReq.getSchedules().size() > 20) {
            throw new ApplicationException(ErrorCode.TOO_MANY_SCHEDULE);
        }

        // Business Logic
        Lecture lecture = memberAndLecture.getLecture();
        boolean check = Boolean.TRUE;
        if(putLectureReq.getSchedules().size() == lecture.getSchedules().size()) {
            for(int i = 0; i < putLectureReq.getSchedules().size(); i++) {
                if(!lecture.getSchedules().get(i).equals(putLectureReq.getSchedules().get(i))) {
                    check = Boolean.FALSE;
                    break;
                }
            }
        }
        else {
            check = Boolean.FALSE;
        }

        if(!lecture.getStartDate().equals(putLectureReq.getStartDate()) || !lecture.getEndDate().equals(putLectureReq.getEndDate()) || !check) {
            if(putLectureReq.getDeleteBefore().equals(Boolean.TRUE)) {
                lessonRepository.deleteAllByLectureIdAndDate(lecture.getLectureId(), lecture.getStartDate(), lecture.getEndDate());
                List<Lesson> lessonList = new ArrayList<>();
                for(LocalDate now = putLectureReq.getStartDate(); now.isBefore(putLectureReq.getEndDate()); now = now.plusDays(1L)) {
                    for(ScheduleVo scheduleVo : putLectureReq.getSchedules()) {
                        if(now.getDayOfWeek().equals(getDayOfWeek(scheduleVo.getWeekday()))) {
                            Schedule schedule = Schedule.convertToSchedule(scheduleVo);
                            Lesson temp = new Lesson(lecture, "Regular", null, null, null, now, schedule);
                            lessonList.add(temp);
                        }
                    }
                }
                lessonJdbcRepository.saveAll(lessonList);
            }
            if(putLectureReq.getDeleteBefore().equals(Boolean.FALSE)) {
                lessonRepository.deleteAllByLectureIdAndDate(lectureId, LocalDate.now(), lecture.getEndDate());
                List<Lesson> lessonList = new ArrayList<>();
                for(LocalDate now = LocalDate.now(); now.isBefore(putLectureReq.getEndDate()); now = now.plusDays(1L)) {
                    for(ScheduleVo scheduleVo : putLectureReq.getSchedules()) {
                        if(now.getDayOfWeek().equals(getDayOfWeek(scheduleVo.getWeekday()))) {
                            Schedule schedule = Schedule.convertToSchedule(scheduleVo);
                            Lesson temp = new Lesson(lecture, "Regular", null, null, null, now, schedule);
                            lessonList.add(temp);
                        }
                    }
                }
                lessonJdbcRepository.saveAll(lessonList);
            }
        }

        lecture.update(putLectureReq);
        memberAndLectureRepository.updateNameAndColorByLectureId(lectureId, putLectureReq.getName(), putLectureReq.getColor());

        // Response
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lectureId).orElse(null);
        return new GetLectureMetaRes(lecture.getLectureId(),
                memberAndLecture.getName(),
                memberAndLecture.getColor(),
                lecture.getSubject(),
                lecture.getSubjectDetail(),
                lecture.getStartDate(),
                lecture.getEndDate(),
                lecture.getSchedules().stream()
                        .map(Schedule::convertToScheduleVo)
                        .collect(Collectors.toList()),
                memberMetas);
    }

    @Transactional
    public GetLectureMainRes updateColor(Member member, Long lectureId, PatchColorReq patchColorReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberIdAndLectureId(member.getMemberId(), lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인
        Lecture lecture = memberAndLecture.getLecture();

        // Business Logic
        memberAndLecture.updateColor(patchColorReq);
        if(memberAndLecture.getIsTeacher().equals(Boolean.TRUE)) {
            lecture.updateColor(patchColorReq);
            memberAndLectureRepository.updateNameAndColorByLectureId(lectureId, lecture.getName(), patchColorReq.getColor());
        }
        else {
            memberAndLecture.updateIsUpdate();
        }
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lectureId).orElse(null);

        // Response
        return new GetLectureMainRes(lecture.getLectureId(), memberAndLecture.getName(), memberAndLecture.getColor(), lecture.getSubject(), memberMetas);
    }

    @Transactional
    public GetLectureMainRes updateName(Member member, Long lectureId, PatchNameReq patchNameReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberIdAndLectureId(member.getMemberId(), lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인
        Lecture lecture = memberAndLecture.getLecture();

        // Business Logic
        memberAndLecture.updateName(patchNameReq);
        if(memberAndLecture.getIsTeacher().equals(Boolean.TRUE)) {
            lecture.updateName(patchNameReq);
            memberAndLectureRepository.updateNameAndColorByLectureId(lectureId, patchNameReq.getName(), lecture.getColor());
        }
        else {
            memberAndLecture.updateIsUpdate();
        }
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lectureId).orElse(null);

        // Response
        return new GetLectureMainRes(lecture.getLectureId(), memberAndLecture.getName(), memberAndLecture.getColor(), lecture.getSubject(), memberMetas);
    }

    public List<GetLectureMainRes> getAllMain(Member member) {
        // Validation

        // Business Logic: member가 해당하는 Class들 조회 -> Class 기본 정보들 다 불러오고, 학생들 정보 역으로 참조해야 함.
        List<Long> lectureIdList = lectureRepository.findAllLectureIdByMember(member)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return lectureRepository.findAllLectureMainByLectureIdList(member, lectureIdList)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
    }

    public List<GetLectureMetaRes> getAllMeta(Member member) {
        // Validation

        // Business Logic
        List<Long> lectureIdList = lectureRepository.findAllLectureIdByMember(member)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return lectureRepository.findAllLectureMetaByLectureIdList(member, lectureIdList)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
    }

    public GetLectureRes getLectureAndLesson(Member member, Long lectureId) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)); // Class 소속 여부 확인

        System.out.println("memberAndLecture: " + memberAndLecture.getMemberAndLectureId());

        // Business Logic
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        List<LessonMetaRes> lessonMetaRess = new ArrayList<>();
        lessonMetaRess.add(lessonRepository.findLessonMetaByLectureIdBeforeNow(lectureId).orElse(null)); // TODO: Optional 사용 시, NullPointException 발생 이유 분석
        lessonMetaRess.add(lessonRepository.findLessonMetaByLectureIdAfterNow(lectureId).orElse(null));

        System.out.println("memberAndLecture2: " + memberAndLecture.getMemberAndLectureId());

        // Response
        return new GetLectureRes(memberAndLecture.getLecture().getLectureId(),
                memberAndLecture.getName(),
                memberAndLecture.getColor(),
                memberAndLecture.getLecture().getSubject(),
                memberAndLecture.getLecture().getSubjectDetail(),
                memberAndLecture.getLecture().getStartDate(),
                memberAndLecture.getLecture().getEndDate(),
                memberAndLecture.getLecture().getRules().stream()
                        .map(Rule::converToRuleVo)
                        .collect(Collectors.toList()),
                memberAndLecture.getLecture().getSchedules().stream()
                        .map(Schedule::convertToScheduleVo)
                        .collect(Collectors.toList()),
                memberMetas,
                lessonMetaRess);
    }

    @Transactional
    public void inviteStudent(Member member, Long lectureId, PostInviteReq postInviteReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));
        if(!member.getType().equals(MemberType.TEACHER)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        // 기존에 초대되었던 학생인지 확인
        if(memberAndLectureRepository.findMemberAndLectureIdByMemberPhoneAndLectureId(postInviteReq.getPhone(), lectureId).orElse(null) != null) {
            throw new ApplicationException(ErrorCode.ALREADY_INVITE_STUDENT);
        }

        // Business Logic
        String lectureName = memberAndLecture.getLecture().getName();
        Member check = memberRepository.findNotFakeByPhoneAndType(postInviteReq.getPhone(), MemberType.STUDENT).orElse(null);
        if(check != null) {
            if(check.getState().equals(MemberState.INVITE)) {
                smsClient.sendInvitation(postInviteReq, Boolean.TRUE);
            }
            if(check.getState().equals(MemberState.ACTIVE) && check.getToken() != null) {
                // smsClient.sendInvitation(member.getName(), lectureName,postInviteReq, Boolean.FALSE);
                String title = lectureName + "클래스 초대";
                String body = "[과릿] " + member.getName() + " 선생님으로부터 " + lectureName + " 클래스 초대가 도착했습니다." + "\n" + "접속하여 초대된 클래스를 확인해보세요!";
                // FCMMessageDto fcmMessageDto = FCMMessageDto.toDto(check.getToken(), title, body, "studentLectureMain", lectureId, null, null, null);
                Message message = fcmClient.makeMessage(check.getToken(), title, body, "studentLectureMain", lectureId, null, null, null, null);
                fcmClient.send(message);
            }
            MemberAndLecture studentAndLecture = MemberAndLecture.builder()
                    .member(check)
                    .lecture(memberAndLecture.getLecture())
                    .build();
            memberAndLectureRepository.save(studentAndLecture);
        }
        if(check == null) {
            smsClient.sendInvitation(postInviteReq, Boolean.TRUE);
            Member student = Member.builder()
                    .name(postInviteReq.getPhone())
                    .type(MemberType.STUDENT.toString())
                    .state(MemberState.INVITE)
                    .phone(postInviteReq.getPhone())
                    .password(UUID.randomUUID().toString())
                    .isAdvertisement(Boolean.FALSE)
                    .isPrivacy(Boolean.FALSE)
                    .build();
            memberRepository.save(student);
            MemberAndLecture studentAndLecture = MemberAndLecture.builder()
                    .member(student)
                    .lecture(memberAndLecture.getLecture())
                    .build();
            memberAndLectureRepository.save(studentAndLecture);
        }

        // Response
    }

    @Transactional
    public void registerStudent(Member member, Long lectureId, PostStudentReq postStudentReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        Member student = Member.builder()
                .name(postStudentReq.getName())
                .type(MemberType.STUDENT.toString())
                .state(MemberState.FAKE)
                .phone(postStudentReq.getPhone())
                .password(UUID.randomUUID().toString())
                .isAdvertisement(Boolean.FALSE)
                .isPrivacy(Boolean.FALSE)
                .build();
        memberRepository.save(student);
        MemberAndLecture studentAndLecture = MemberAndLecture.builder()
                .member(student)
                .lecture(memberAndLecture.getLecture())
                .build();
        memberAndLectureRepository.save(studentAndLecture);

        // Response
    }

    @Transactional
    public void emitStudent(Member member, Long lectureId, List<PostStudentIdReq> postStudentIdReqList) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<Long> memberIdList = postStudentIdReqList.stream()
                .map(PostStudentIdReq::getMemberId)
                .collect(Collectors.toList());
        memberAndLectureRepository.deleteMemberAndLectureByMemberIdList(lectureId, memberIdList);
        memberRepository.deleteMemberByMemberIdList(memberIdList);

        // Response
    }

    public List<GetStudentRes> getStudent(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic && Response
        return memberAndLectureRepository.findStudentByMemberAndLectureId(member, lectureId).orElse(null);
    }


    // DB 저장된 Json 객체에서 요일 뽑아오기 위한 메소드
    public DayOfWeek getDayOfWeek(Day day) {
        switch (day) {
            case MON:
                return DayOfWeek.MONDAY;
            case TUE:
                return DayOfWeek.TUESDAY;
            case WED:
                return DayOfWeek.WEDNESDAY;
            case THU:
                return DayOfWeek.THURSDAY;
            case FRI:
                return DayOfWeek.FRIDAY;
            case SAT:
                return DayOfWeek.SATURDAY;
            case SUN:
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Invalid Day enum value: " + day);
        }
    }
}
