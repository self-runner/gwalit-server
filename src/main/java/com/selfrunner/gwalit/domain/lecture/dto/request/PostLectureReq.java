package com.selfrunner.gwalit.domain.lecture.dto.request;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lecture.entity.Rule;
import com.selfrunner.gwalit.domain.lecture.entity.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostLectureReq {

    @NotNull
    private String name;

    private String color;

    private String month;

    private List<Rule> rules;

    private List<Schedule> schedules;

    public Lecture toEntity() {
        Lecture lecture = Lecture.builder()
                .name(this.name)
                .color(this.color)
                .month(Integer.valueOf(this.month))
                .rules(this.rules)
                .schedules(this.schedules)
                .build();

        return lecture;
    }
}
