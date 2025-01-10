package com.selfrunner.domainmodule.domain.workbook.repository;

import com.selfrunner.domainmodule.domain.workbook.Workbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkbookRepository extends JpaRepository<Workbook, Long>, WorkbookRepositoryCustom {
}
