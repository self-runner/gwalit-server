package com.selfrunner.gwalit.domain.task.repository;

import com.selfrunner.gwalit.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

    Optional<List<Task>> findTasksByLectureLectureIdOrderByDeadlineDesc(Long lectureId);

}
