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

    private String taskId;

    private String lectureId;

    private String title;

    private LocalDate deadline;

    private Boolean isPinned;

    private List<Subtask> subtasks;

    public TaskRes(Task task) {
        this.taskId = task.getTaskId().toString();
        this.lectureId = task.getLecture().getLectureId().toString();
        this.title = task.getTitle();
        this.deadline = task.getDeadline();
        this.isPinned = task.getIsPinned();
        this.subtasks = task.getSubtasks();
    }


}
