package com.selfrunner.gwalit.domain.lecture.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
@Tag(name = "Lecture", description = "Class 기본 정보 관련")
public class LectureController {
}
