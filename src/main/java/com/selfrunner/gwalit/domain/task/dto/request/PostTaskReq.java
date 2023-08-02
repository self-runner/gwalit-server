package com.selfrunner.gwalit.domain.task.dto.request;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.task.entity.Subtask;
import com.selfrunner.gwalit.domain.task.entity.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostTaskReq {

    @NotEmpty(message = "연결된 Class가 없습니다.")
    private String lectureId;

    @NotEmpty(message = "할 일 제목이 입력되지 않았습니다.")
    private String title;

    private LocalDate deadline;

    private List<Subtask> subtasks;

    public Task toEntity(Lecture lecture)  {
        Task task = Task.builder()
                .lecture(lecture)
                .title(this.title)
                .deadline(this.deadline)
                .subtasks(this.subtasks)
                .build();

        return task;
    }
}
