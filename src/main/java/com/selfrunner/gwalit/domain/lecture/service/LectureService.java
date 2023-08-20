package com.selfrunner.gwalit.domain.lecture.service;


import com.selfrunner.gwalit.domain.homework.repository.HomeworkRepository;
import com.selfrunner.gwalit.domain.lecture.dto.request.PostLectureReq;
import com.selfrunner.gwalit.domain.lecture.dto.request.PostStudentReq;
import com.selfrunner.gwalit.domain.lecture.dto.request.PutLectureReq;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMainRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMetaRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureRes;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lecture.repository.LectureRepository;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        Lecture lecture = postLectureReq.toEntity();
        MemberAndLecture memberAndLecture = MemberAndLecture.builder()
                        .member(member)
                        .lecture(lecture)
                        .build();
        lectureRepository.save(lecture);
        memberAndLectureRepository.save(memberAndLecture);

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
        memberAndLecture.getLecture().update(putLectureReq);

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

        // DB에 저장된 리포트 중 가장 최근 리포트 조회 로직 (Old ver.)
//        LessonMetaRes lessonMetaRes = lessonRepository.findLessonMetaByLectureId(lectureId); // TODO: Optional 사용 시, NullPointException 발생 이유 분석
//        GetLectureRes getLectureRes = new GetLectureRes(memberAndLecture.getLecture(), memberMetas, lessonMetaRes);

        // 일주일 중 가장 최근 데이터 조회하는 로직 (New ver.)
        List<LessonMetaRes> lessonMetaResList = lessonRepository.findAllLessonMetaByLectureIdAndDate(lectureId).orElse(null); // 일주일 기간에 해당하는 모든 LessonMeta 조회
        // 해당하는 일주일의 기간 중 수업 주기와 일치하는 날짜 뽑아오기
        List<Schedule> schedules = memberAndLecture.getLecture().getSchedules();
        for(LocalDate date = LocalDate.now().minusWeeks(1l).plusDays(1l); date.isBefore(LocalDate.now().plusDays(1l)); date = date.plusDays(1l)) {
            if(date.isBefore(memberAndLecture.getLecture().getStartDate())) {
                continue;
            }
            for(Schedule s : schedules) {
                if (date.getDayOfWeek().equals(getDayOfWeek(s.getWeekday()))) {
                    Boolean check = true;
                    for(LessonMetaRes l : lessonMetaResList) {
                        if(l.getDate().equals(date)) {
                            check = false;
                            break;
                        }
                    }
                    if(check) {
                        LessonMetaRes temp = new LessonMetaRes(null, lectureId, LessonType.Regular, date, s, null);
                        lessonMetaResList.add(temp);
                    }
                }
            }
        }
        Collections.sort(lessonMetaResList); // 오류 발생 지점
        int idx = -1;
        for(int i = 0; i < lessonMetaResList.size(); i++) {
            if(!lessonMetaResList.get(i).getType().equals(LessonType.Deleted)) {
                idx = i;
                break;
            }
        }

        GetLectureRes getLectureRes = (idx != -1) ?  new GetLectureRes(memberAndLecture.getLecture(), memberMetas, lessonMetaResList.get(idx)) : new GetLectureRes(memberAndLecture.getLecture(), memberMetas, null);

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
