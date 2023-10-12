package com.selfrunner.gwalit.domain.task.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selfrunner.gwalit.domain.task.entity.Subtask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PutTaskReq {

    @NotEmpty(message = "할 일 제목이 입력되지 않았습니다.")
    @Size(min = 1, max = 50, message = "할 일 제목의 글자수는 1자 ~ 50자 사이여야 합니다.")
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate deadline;

    @NotNull
    private Boolean isPinned;

    private List<Subtask> subtasks;
}
