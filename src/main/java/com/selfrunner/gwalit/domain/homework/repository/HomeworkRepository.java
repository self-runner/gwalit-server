package com.selfrunner.gwalit.domain.homework.repository;

import com.selfrunner.gwalit.domain.homework.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeworkRepository extends JpaRepository<Homework, Long>, HomeworkRepositoryCustom {
}
