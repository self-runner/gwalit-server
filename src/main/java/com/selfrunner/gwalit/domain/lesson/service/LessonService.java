package com.selfrunner.gwalit.domain.lesson.service;

import com.selfrunner.gwalit.domain.lesson.dto.request.PostLessonReq;
import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    public Void register(Member member, PostLessonReq postLessonReq) {
        // Validation

        // Business Logic

        // Response
        return null;
    }
}
