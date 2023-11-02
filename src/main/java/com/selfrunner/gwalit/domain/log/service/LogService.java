package com.selfrunner.gwalit.domain.log.service;

import com.selfrunner.gwalit.domain.log.dto.LogReq;
import com.selfrunner.gwalit.domain.log.entity.Log;
import com.selfrunner.gwalit.domain.log.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional
    public void register(LogReq logReq) {
        // Business Logic
        Log log = logReq.toEntity();
        logRepository.save(log);

        // Response
    }
}
