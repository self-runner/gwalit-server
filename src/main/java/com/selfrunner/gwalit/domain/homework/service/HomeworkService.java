package com.selfrunner.gwalit.domain.homework.service;

import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkReq;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkMainRes;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
import com.selfrunner.gwalit.domain.homework.entity.Homework;
import com.selfrunner.gwalit.domain.homework.exception.HomeworkException;
import com.selfrunner.gwalit.domain.homework.repository.HomeworkRepository;
import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.domain.lesson.exception.LessonException;
import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import com.selfrunner.gwalit.domain.member.exception.MemberException;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final LessonRepository lessonRepository;
    private final MemberAndLectureRepository memberAndLectureRepository;

    @Transactional
    public Void register(Member member, Long lessonId, HomeworkReq homeworkReq) {
        // Validation
        if(lessonId != null) {
            Lesson lesson = lessonRepository.findById(Long.valueOf(lessonId)).orElseThrow(() -> new LessonException(ErrorCode.NOT_EXIST_LESSON)); // 해당 수업이 미존재 시, 에러 반환
            memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION)); // 해당 수업과 관련된 클래스에 권한 없는 경우 에러 반환
        }

        // Business Logic
        Homework homework = homeworkReq.toEntity(member, lessonId);
        homeworkRepository.save(homework);

        // Response
        return null;
    }

    @Transactional
    public HomeworkMainRes update(Member member, Long homeworkId, HomeworkReq homeworkReq) {
        // Validation
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(() -> new HomeworkException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(!homework.getMemberId().equals(member.getMemberId())) {
            throw new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        homework.update(homeworkReq);
        HomeworkMainRes homeworkMainRes = homeworkRepository.findHomeworkByHomeworkId(homeworkId);

        // Response
        return homeworkMainRes;
    }

    @Transactional
    public Void delete(Member member, Long homeworkId) {
        // Validation
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(() -> new HomeworkException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        homeworkRepository.delete(homework);

        // Response
        return null;
    }

    public HomeworkRes get(Member member, Long homeworkId) {
        // Validation
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(() -> new HomeworkException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(!homework.getMemberId().equals(member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        HomeworkRes homeworkRes = new HomeworkRes(homework);

        // Response
        return homeworkRes;
    }

    public List<HomeworkRes> getAll(Member member) {
        // Validation
        if(member.getType().equals(MemberType.TEACHER)) { // 학생용 API 검증
            throw new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        List<Homework> homeworkList = homeworkRepository.findAllByMemberId(member.getMemberId()).orElseThrow(() -> new HomeworkException(ErrorCode.NOT_FOUND_EXCEPTION));
        List<HomeworkRes> homeworkResList = homeworkList.stream()
                .map(HomeworkRes::new)
                .collect(Collectors.toList());

        // Response
        return homeworkResList;
    }

    public List<HomeworkMainRes> getMain(Member member) {
        // Validation

        // Business Logic
        List<Long> lectureIdList = memberAndLectureRepository.findLectureIdByMember(member).orElse(null);
        List<Long> lessonIdList = lessonRepository.findRecentLessonIdByLectureIdList(lectureIdList).orElse(null);
        for(Long id: lessonIdList) {
            System.out.println(id);
        }
        List<HomeworkMainRes> homeworkMainResList = homeworkRepository.findRecentHomeworkByMemberAndLessonIdList(member, lessonIdList).orElse(null);

        // Response
        return homeworkMainResList;
    }

    public List<HomeworkMainRes> getLecture(Member member, Long lectureId) {
        // Validation
        memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lectureId).orElseThrow(() -> new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION));

        // Business Logic
        List<Long> lessonIdList = new ArrayList<>();
        Long lessonId = lessonRepository.findRecentLessonIdByLectureId(lectureId).orElse(null);
        if(lessonId != null) {
            lessonIdList.add(lessonId);
        }
        List<HomeworkMainRes> homeworkMainResList = homeworkRepository.findRecentHomeworkByMemberAndLessonIdList(member, lessonIdList).orElse(null);


        // Response
        return homeworkMainResList;
    }

    public List<HomeworkMainRes> getList(Member member, String type) {
        // Validation - 학생용 API
        if(member.getType().equals(MemberType.TEACHER)) {
            throw new MemberException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic - all: 전체 리스트 / finished: 완료 리스트 / unfinished: 미완료 리스트
        List<HomeworkMainRes> homeworkMainResList = new ArrayList<>();
        if(type.equals("all")) {
            homeworkMainResList = homeworkRepository.findAllHomeworkByMember(member).orElse(null);
        }
        if(type.equals("finished")) {
            homeworkMainResList = homeworkRepository.findAllHomeworkByMemberAndType(member, Boolean.TRUE).orElse(null);
        }
        if(type.equals("unfinished")) {
            homeworkMainResList = homeworkRepository.findAllHomeworkByMemberAndType(member, Boolean.FALSE).orElse(null);
        }


        // Response
        return homeworkMainResList;
    }
}
