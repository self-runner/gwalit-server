package com.selfrunner.gwalit.domain.workbook.repository;

import com.selfrunner.gwalit.domain.workbook.entity.WorkbookAndProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkbookAndProblemRepository extends JpaRepository<WorkbookAndProblem, Long>, WorkbookAndProblemRepositoryCustom {
}
