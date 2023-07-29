package com.selfrunner.gwalit.domain.lecture.dto.request;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lecture.entity.Rule;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostLectureReq {

    @NotEmpty(message = "Class 이름이 공란입니다.")
    private String name;

    @NotEmpty(message = "Class 색상이 미정입니다.")
    private String color;

    @NotEmpty(message = "기간이 설정되지 않았습니다.")
    private String month;

    private List<Rule> rules;

    @NotEmpty(message = "수업 일정이 지정되지 않았습니다.")
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
