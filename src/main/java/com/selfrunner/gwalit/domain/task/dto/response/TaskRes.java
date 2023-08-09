package com.selfrunner.gwalit.domain.task.dto.response;

import com.selfrunner.gwalit.domain.task.entity.Subtask;
import com.selfrunner.gwalit.domain.task.entity.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class TaskRes {

    private final Long taskId;

    private final Long lectureId;

    private final String color;

    private final String title;

    private final LocalDate deadline;

    private final Boolean isPinned;

    private final List<Subtask> subtasks;
}
