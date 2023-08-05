package com.selfrunner.gwalit.domain.lesson.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkReq;
import com.selfrunner.gwalit.domain.lesson.entity.Progress;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PutLessonReq {

    @NotNull(message = "수업 유형이 선택되지 않았습니다.")
    @Pattern(regexp = "(Regular|Makeup|Deleted)", message = "올바르지 않은 수업 유형입니다.")
    private String type;

    @NotNull(message = "선택된 참여자가 없습니다.")
    private List<Long> participants;

    private String feedback;

    private List<Progress> progresses;

    private List<HomeworkReq> homeworks;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private Schedule time;
}
