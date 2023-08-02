package com.selfrunner.gwalit.domain.task.repository;

import com.selfrunner.gwalit.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {
}
