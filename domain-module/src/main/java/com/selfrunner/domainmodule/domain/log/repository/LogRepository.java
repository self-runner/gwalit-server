package com.selfrunner.domainmodule.domain.log.repository;

import com.selfrunner.domainmodule.domain.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
