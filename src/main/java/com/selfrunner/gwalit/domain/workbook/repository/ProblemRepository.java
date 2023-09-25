package com.selfrunner.gwalit.domain.workbook.repository;

import com.selfrunner.gwalit.domain.workbook.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long>, ProblemRepositoryCustom {
}
