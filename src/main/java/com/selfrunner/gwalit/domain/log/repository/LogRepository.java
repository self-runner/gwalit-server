package com.selfrunner.gwalit.domain.log.repository;

import com.selfrunner.gwalit.domain.log.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
