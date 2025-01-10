package com.selfrunner.commonmodule.dto.lecture.request;


import com.selfrunner.commonmodule.vo.lecture.RuleVo;
import com.selfrunner.commonmodule.vo.lecture.ScheduleVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PutLectureReq {

    @NotEmpty(message = "Class 이름이 공란입니다.")
    @Size(min = 2, max = 16, message = "클래스명은 2자 ~ 16자 사이여야 합니다.")
    private String name;

    @NotEmpty(message = "Class 색상이 미정입니다.")
    private String color;

    @NotNull(message = "시작일자가 설정되지 않았습니다.")
    private LocalDate startDate;

    @NotEmpty(message = "과목이 선택되지 않았습니다.")
    private String subject;

    private String subjectDetail;

    private LocalDate endDate;

    private List<RuleVo> rules;

    private List<ScheduleVo> schedules;

    @NotNull
    private Boolean deleteBefore;
}
