package com.selfrunner.gwalit.domain.homework.service;

import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkReq;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
import com.selfrunner.gwalit.domain.homework.entity.Homework;
import com.selfrunner.gwalit.domain.homework.repository.HomeworkRepository;
import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            Lesson lesson = lessonRepository.findById(Long.valueOf(lessonId)).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_EXIST_LESSON)); // 해당 수업이 미존재 시, 에러 반환
            memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(member, lesson.getLecture().getLectureId()).orElseThrow(() -> new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)); // 해당 수업과 관련된 클래스에 권한 없는 경우 에러 반환
        }

        // Business Logic
        Homework homework = homeworkReq.toEntity(member, lessonId);
        homeworkRepository.save(homework);

        // Response
        return null;
    }

    @Transactional
    public Void update(Member member, Long homeworkId, HomeworkReq homeworkReq) {
        // Validation
        /*
        Todo: 학생 수정 권한 부여 여부 필요성 확인
         */
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        homework.update(homeworkReq);

        // Response
        return null;
    }

    @Transactional
    public Void delete(Member member, Long homeworkId) {
        // Validation
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        homeworkRepository.delete(homework);

        // Response
        return null;
    }

    public HomeworkRes get(Member member, Long homeworkId) {
        // Validation
        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(!homework.getMemberId().equals(member.getMemberId())) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        // Business Logic
        HomeworkRes homeworkRes = new HomeworkRes().toDto(homework);

        // Response
        return  homeworkRes;
    }
}
