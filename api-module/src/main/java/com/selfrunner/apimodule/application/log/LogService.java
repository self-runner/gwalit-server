package com.selfrunner.apimodule.application.log;

import com.selfrunner.commonmodule.dto.log.LogReq;
import com.selfrunner.domainmodule.domain.log.Log;
import com.selfrunner.domainmodule.domain.log.repository.LogRepository;
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
        Log log = Log.builder()
                .memberId(logReq.getMemberId())
                .type(logReq.getType())
                .endPoint(logReq.getEndPoint())
                .isSuccess(logReq.getIsSuccess())
                .build();

        logRepository.save(log);

        // Response
    }
}
