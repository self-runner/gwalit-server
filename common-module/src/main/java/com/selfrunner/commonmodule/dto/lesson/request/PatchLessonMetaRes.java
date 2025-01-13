package com.selfrunner.commonmodule.dto.lesson.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selfrunner.commonmodule.vo.lecture.ScheduleVo;
import com.selfrunner.commonmodule.vo.lesson.Participant;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PatchLessonMetaRes {

    private List<Participant> participants;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private ScheduleVo time;
}
