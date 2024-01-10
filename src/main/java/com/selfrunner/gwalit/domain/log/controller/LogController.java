package com.selfrunner.gwalit.domain.log.controller;

import com.selfrunner.gwalit.domain.log.dto.LogReq;
import com.selfrunner.gwalit.domain.log.service.LogService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/log")
@Tag(name = "Log", description = "API 로깅")
public class LogController {

    private final LogService logService;

    @Operation(description = "로그 등록")
    @PostMapping("")
    public ApplicationResponse<Void> register(@Valid @RequestBody LogReq logReq ) {
        logService.register(logReq);
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }

}
