package com.selfrunner.gwalit.domain.lesson.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkReq;
import com.selfrunner.gwalit.domain.lesson.entity.Participant;
import com.selfrunner.gwalit.domain.lesson.entity.Progress;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PutLessonReq {

    @NotNull(message = "수업 유형이 선택되지 않았습니다.")
    @Pattern(regexp = "(Regular|Makeup)", message = "올바르지 않은 수업 유형입니다.")
    private String type;

    private List<Participant> participants;

    @Size(max = 500, message = "피드백은 최대 500자까지만 작성할 수 있습니다.")
    private String feedback;

    private List<Progress> progresses;

    private List<HomeworkReq> homeworks;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private Schedule time;
}
