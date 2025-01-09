package com.selfrunner.domainmodule.domain.task.repository;

import com.selfrunner.domainmodule.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

    void deleteAllByLectureLectureId(Long lectureId);
}
