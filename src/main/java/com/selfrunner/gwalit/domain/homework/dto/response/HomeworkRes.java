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

    public HomeworkRes toDto(Homework homework) {
        HomeworkRes homeworkRes = new HomeworkRes();
        homeworkRes.homeworkId = homework.getHomeworkId().toString();
        homeworkRes.lessonId = homework.getLessonId().toString();
        homeworkRes.memberId = homework.getMemberId().toString();
        homeworkRes.body = homework.getBody();
        homeworkRes.deadline = homework.getDeadline();
        homeworkRes.isFinish = homework.getIsFinish();

        return homeworkRes;
    }
}
