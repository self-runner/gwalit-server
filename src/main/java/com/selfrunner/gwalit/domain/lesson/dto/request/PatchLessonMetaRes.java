package com.selfrunner.gwalit.domain.lesson.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selfrunner.gwalit.domain.lesson.entity.Participant;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PatchLessonMetaRes {

    private List<Participant> participants;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private Schedule time;
}
