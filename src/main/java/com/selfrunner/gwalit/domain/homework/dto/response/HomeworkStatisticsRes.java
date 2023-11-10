package com.selfrunner.gwalit.domain.homework.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HomeworkStatisticsRes {

    private final Long homeworkId;

    private final Long memberId;

    private final Long lessonId;

    private final String body;
}
