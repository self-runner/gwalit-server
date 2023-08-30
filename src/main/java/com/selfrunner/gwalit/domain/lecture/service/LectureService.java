package com.selfrunner.gwalit.domain.lecture.service;


import com.selfrunner.gwalit.domain.homework.repository.HomeworkRepository;
import com.selfrunner.gwalit.domain.lecture.dto.request.PostLectureReq;
import com.selfrunner.gwalit.domain.lecture.dto.request.PostStudentIdReq;
import com.selfrunner.gwalit.domain.lecture.dto.request.PostStudentReq;
import com.selfrunner.gwalit.domain.lecture.dto.request.PutLectureReq;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMainRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMetaRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureRes;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lecture.repository.LectureRepository;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.domain.lesson.entity.LessonType;
import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.domain.task.repository.TaskRepository;
import com.selfrunner.gwalit.global.common.Day;
import com.selfrunner.gwalit.global.common.Schedule;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private final HomeworkRepository homeworkRepository;

    @Transactional
    public Void register(Member member, PostLectureReq postLectureReq) {
        // Valid
        if(member.getType() != MemberType.TEACHER) { // 방 생성 권한 없음
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        if(memberAndLectureRepository.findCountByMember(member) > 3) {
            throw new ApplicationException(ErrorCode.FAILED_MAKE_CLASS);
        }

        // Business Logic
        Lecture lecture = lectureRepository.save(postLectureReq.toEntity());
        MemberAndLecture memberAndLecture = MemberAndLecture.builder()
                        .member(member)
                        .lecture(lecture)
                        .build();
        memberAndLectureRepository.save(memberAndLecture);
        List<Lesson> lessonList = new ArrayList<>();
        for(LocalDate now = postLectureReq.getStartDate(); now.isBefore(postLectureReq.getEndDate()); now = now.plusDays(1L)) {
            for(Schedule schedule : postLectureReq.getSchedules()) {
                if(now.getDayOfWeek().equals(getDayOfWeek(schedule.getWeekday()))) {
                    Lesson temp = new Lesson(lecture, "Regular", null, null, null, now, schedule);
                    lessonList.add(temp);
                }
            }
        }
        lessonRepository.saveAll(lessonList);

        // Response
        return null;
    }

    @Transactional
    public Void delete(Member member, Long lectureId) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인

        // Business Logic
        memberAndLectureRepository.delete(memberAndLecture);
        taskRepository.deleteAllByLectureLectureId(lectureId);
        List<Long> lessonIdList = lessonRepository.findAllLessonIdByLectureId(lectureId);
        homeworkRepository.deleteAllByLessonIdList(lessonIdList);
        lessonRepository.deleteAllByLectureLectureId(lectureId);
        lectureRepository.delete(memberAndLecture.getLecture());

        // Response
        return null;
    }

    public GetLectureMetaRes get(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인

        // Business Logic
        /*
        TODO: 쿼리 튜닝을 통한 성능향상 필요
         */
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS));
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        GetLectureMetaRes getLectureMetaRes = new GetLectureMetaRes(lecture.getLectureId(), lecture.getName(), lecture.getColor(), lecture.getStartDate(), lecture.getEndDate(), lecture.getSchedules(), memberMetas);

        // Response
        return getLectureMetaRes;
    }

    @Transactional
    public Void update(Member member, Long lectureId, PutLectureReq putLectureReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_CLASS)); // Class 소속 여부 확인

        // Business Logic
        Lecture lecture = memberAndLecture.getLecture();
        // TODO: 이전 수업 리포트들도 다 지울 것인지, 아닌지에 따른 조건 분기
        if(!lecture.getStartDate().equals(putLectureReq.getStartDate()) || !lecture.getEndDate().equals(putLectureReq.getEndDate())) {
            if(putLectureReq.getDeleteBefore().equals(Boolean.TRUE)) {
                lessonRepository.deleteAllByLectureIdAndDate(lecture.getLectureId(), lecture.getStartDate(), lecture.getEndDate());
                List<Lesson> lessonList = new ArrayList<>();
                for(LocalDate now = putLectureReq.getStartDate(); now.isBefore(putLectureReq.getEndDate()); now = now.plusDays(1L)) {
                    for(Schedule schedule : putLectureReq.getSchedules()) {
                        if(now.getDayOfWeek().equals(getDayOfWeek(schedule.getWeekday()))) {
                            Lesson temp = new Lesson(lecture, "Regular", null, null, null, now, schedule);
                            lessonList.add(temp);
                        }
                    }
                }
                lessonRepository.saveAll(lessonList);
            }
            if(putLectureReq.getDeleteBefore().equals(Boolean.FALSE)) {
                lessonRepository.deleteAllByLectureIdAndDate(lectureId, LocalDate.now(), lecture.getEndDate());
                List<Lesson> lessonList = new ArrayList<>();
                for(LocalDate now = LocalDate.now(); now.isBefore(putLectureReq.getEndDate()); now = now.plusDays(1L)) {
                    for(Schedule schedule : putLectureReq.getSchedules()) {
                        if(now.getDayOfWeek().equals(getDayOfWeek(schedule.getWeekday()))) {
                            Lesson temp = new Lesson(lecture, "Regular", null, null, null, now, schedule);
                            lessonList.add(temp);
                        }
                    }
                }
                lessonRepository.saveAll(lessonList);
            }
        }

        lecture.update(putLectureReq);

        // Response
        return null;
    }

    public List<GetLectureMainRes> getAllMain(Member member) {
        // Validation

        // Business Logic: member가 해당하는 Class들 조회 -> Class 기본 정보들 다 불러오고, 학생들 정보 역으로 참조해야 함.
        List<Long> lectureIdList = lectureRepository.findAllLectureIdByMember(member).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        List<GetLectureMainRes> getLectureMainResList = lectureRepository.findAllLectureMainByLectureIdList(lectureIdList).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return getLectureMainResList;
    }

    public List<GetLectureMetaRes> getAllMeta(Member member) {
        // Validation

        // Business Logic
        List<Long> lectureIdList = lectureRepository.findAllLectureIdByMember(member).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        List<GetLectureMetaRes> getLectureMetaRes = lectureRepository.findAllLectureMetaByLectureIdList(lectureIdList).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return getLectureMetaRes;
    }

    public GetLectureRes getLectureAndLesson(Member member, Long lectureId) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)); // Class 소속 여부 확인

        // Business Logic
        List<MemberMeta> memberMetas = memberAndLectureRepository.findMemberMetaByLectureLectureId(lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        List<LessonMetaRes> lessonMetaRess = new ArrayList<>();
        lessonMetaRess.add(lessonRepository.findLessonMetaByLectureIdBeforeNow(lectureId).orElse(null)); // TODO: Optional 사용 시, NullPointException 발생 이유 분석
        lessonMetaRess.add(lessonRepository.findLessonMetaByLectureIdAfterNow(lectureId).orElse(null));
        GetLectureRes getLectureRes = new GetLectureRes(memberAndLecture.getLecture(), memberMetas, lessonMetaRess);

        // Response
        return getLectureRes;
    }

    /*
    TODO: 중간 심의 이후 적용 예정
     */
//    @Transactional
//    public Void inviteStudent(Member member, PostInviteReq postInviteReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
//        // Validation
//        if (member.getType() != MemberType.TEACHER) {
//            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
//        }
//
//        // Business Logic: MemberAndLecture 정보 저장 미 필요 (학생이 초대되었을 때는 연결될 필요 없지 않나..?)
//        smsClient.sendInvitiation(member.getName(), postInviteReq);
//        Member student = new Member();
//        memberRepository.save(student);
//        MemberAndLecture memberAndLecture = MemberAndLecture.builder()
//                .member(member)
//                .lecture()
//                .build();
//
//        // Response
//        return null;
//    }

    @Transactional
    public Void registerStudent(Member member, Long lectureId, PostStudentReq postStudentReq) {
        // Validation
        MemberAndLecture memberAndLecture = memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        /*
        1. 가계정 생성
        2. 가계정 등록
        3. 학생 관리 정보 반환 시 가계정 여부 포함해서 반환하도록 수정
        TODO: java.lang.NullPointerException: Name is null 해결
         */
        Member student = postStudentReq.toEntity();
        memberRepository.save(student);
        MemberAndLecture studentAndLecture = MemberAndLecture.builder()
                .member(student)
                .lecture(memberAndLecture.getLecture())
                .build();
        memberAndLectureRepository.save(studentAndLecture);

        // Response
        return null;
    }

    @Transactional
    public Void emitStudent(Member member, Long lectureId, List<PostStudentIdReq> postStudentIdReqList) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<Long> memberIdList = postStudentIdReqList.stream()
                .map(postStudentIdReq -> postStudentIdReq.getMemberId())
                .collect(Collectors.toList());
        memberAndLectureRepository.deleteMemberAndLectureByMemberIdList(memberIdList);
        memberRepository.deleteMemberByMemberIdList(memberIdList);

        // Response
        return null;
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
