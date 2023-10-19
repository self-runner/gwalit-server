package com.selfrunner.gwalit.domain.workbook.repository;

import com.selfrunner.gwalit.domain.workbook.entity.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkbookRepository extends JpaRepository<Workbook, Long>, WorkbookRepositoryCustom {
}
