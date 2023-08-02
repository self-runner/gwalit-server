package com.selfrunner.gwalit.domain.task.repository;

import com.selfrunner.gwalit.domain.task.entity.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtaskRepository extends JpaRepository<Subtask, Long>, SubtaskRepositoryCustom {
}
