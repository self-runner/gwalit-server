package com.selfrunner.gwalit.domain.lecture.dto.request;

import com.selfrunner.gwalit.domain.lecture.entity.Rule;
import com.selfrunner.gwalit.domain.lecture.entity.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PutLectureReq {

    private String name;

    private String color;

    private String month;

    private List<Rule> rules;

    private List<Schedule> schedules;
}
