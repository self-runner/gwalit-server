package com.selfrunner.gwalit.domain.lecture.dto.response;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lecture.entity.Rule;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetLectureRes {

    private String lectureId;

    private String name;

    private String color;

    private String month;

    private List<Rule> rules;

    private List<Schedule> schedules;

    public GetLectureRes(Lecture lecture) {
        this.lectureId = lecture.getLectureId().toString();
        this.name = lecture.getName();
        this.color = lecture.getColor();
        this.month = lecture.getMonth().toString();
        this.rules = lecture.getRules();
        this.schedules = lecture.getSchedules();
    }
}
