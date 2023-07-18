package com.selfrunner.gwalit.global.common;

import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private Integer code;
    private String message;

    public ErrorResponse(ErrorCode errorcode) {
        this.timestamp = LocalDateTime.now().withNano(0);
        this.code = errorcode.getCode();
        this.message = errorcode.getMessage();
    }

    public ErrorResponse(String message) {
        this.timestamp = LocalDateTime.now().withNano(0);
        this.code = 2000;
        this.message = message;
    }
}
