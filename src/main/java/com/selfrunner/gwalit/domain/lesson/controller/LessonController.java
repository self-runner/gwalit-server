package com.selfrunner.gwalit.domain.lesson.controller;

import com.selfrunner.gwalit.domain.lesson.service.LessonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson")
@Tag(name = "Lecture", description = "수업 별 기본 정보 관련")
public class LessonController {

    private final LessonService lessonService;
}
