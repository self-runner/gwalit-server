package com.selfrunner.gwalit.domain.homework.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class HomeworkStatisticsRes {

    private final Long homeworkId;

    private final Long memberId;

    private final String name;

    private final Long lessonId;

    private final String body;

    private final LocalDate deadline;

    private final Boolean isFinish;
}
