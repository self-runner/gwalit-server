package com.selfrunner.gwalit.domain.homework.dto.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class HomeworkRemind {

    private final Long homeworkId;

    private final Long lectureId;

    private final Long lectureName;

    private final Long lessonId;

    private final Long memberId;

    private final String token;

    private final String body;

    private final LocalDate deadline;

    private final Boolean isFinish;
}
