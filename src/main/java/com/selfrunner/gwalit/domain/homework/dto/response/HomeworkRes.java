package com.selfrunner.gwalit.domain.homework.dto.response;

import com.selfrunner.gwalit.domain.homework.entity.Homework;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class HomeworkRes {

    private String homeworkId;

    private String lessonId;

    private String memberId;

    private String body;

    private LocalDate deadline;

    private Boolean isFinish;

    public HomeworkRes(Homework homework) {
        this.homeworkId = homework.getHomeworkId().toString();
        this.lessonId = (homework.getLessonId() == null) ? null : homework.getLessonId().toString();
        this.memberId = homework.getMemberId().toString();
        this.body = homework.getBody();
        this.deadline = homework.getDeadline();
        this.isFinish = homework.getIsFinish();
    }
}
