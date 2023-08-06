package com.selfrunner.gwalit.domain.homework.dto.response;

import com.selfrunner.gwalit.domain.homework.entity.Homework;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class HomeworkRes {

    private final Long homeworkId;

    private final Long lessonId;

    private final Long memberId;

    private final String body;

    private final LocalDate deadline;

    private final Boolean isFinish;

    public HomeworkRes(Homework homework) {
        this.homeworkId = homework.getHomeworkId();
        this.lessonId = (homework.getLessonId() == null) ? null : homework.getLessonId();
        this.memberId = homework.getMemberId();
        this.body = homework.getBody();
        this.deadline = homework.getDeadline();
        this.isFinish = homework.getIsFinish();
    }
}
