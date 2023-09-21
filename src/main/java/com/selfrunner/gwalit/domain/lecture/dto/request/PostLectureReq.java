package com.selfrunner.gwalit.domain.lecture.dto.request;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lecture.entity.Rule;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostLectureReq {

    @NotEmpty(message = "Class 이름이 공란입니다.")
    private String name;

    @NotEmpty(message = "Class 색상이 미정입니다.")
    private String color;

    @NotEmpty(message = "과목이 선택되지 않았습니다.")
    private String subject;

    private String subjectDetail;

    @NotNull(message = "시작일자가 설정되지 않았습니다.")
    private LocalDate startDate;

    private LocalDate endDate;

    private List<Rule> rules;

    private List<Schedule> schedules;

    public Lecture toEntity() {
        Lecture lecture = Lecture.builder()
                .name(this.name)
                .color(this.color)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .rules(this.rules)
                .schedules(this.schedules)
                .build();

        return lecture;
    }
}
