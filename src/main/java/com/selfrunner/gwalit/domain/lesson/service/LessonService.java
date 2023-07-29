package com.selfrunner.gwalit.domain.lesson.service;

import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
}
