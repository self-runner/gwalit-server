package com.selfrunner.gwalit.domain.homework.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class HomeworkMainRes {

    private final Long homeworkId;

    private final Long lectureId;

    private final String color;

    private final Long lessonId;

    private final Long memberId;

    private final String body;

    private final LocalDate deadline;

    private final Boolean isFinish;
}
