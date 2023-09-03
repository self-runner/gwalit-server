package com.selfrunner.gwalit.global.common;

import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicationResponse<T> {
    private final LocalDateTime timestamp;
    private final Integer code;
    private final String message;
    private final T data;

    // 200: 반환할 값이 존재하는 경우
    public static <T> ApplicationResponse<T> ok(ErrorCode errorCode, T data) {
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(data)
                .build();
    }

    // 200: 반환할 값이 없는 경우
    public static <T> ApplicationResponse<T> ok(ErrorCode errorCode) {
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }

    // 201: 반환할 값이 없는 경우
    public static <T> ApplicationResponse<T> create(ErrorCode errorCode) {
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }

    // 201: 반환할 값이 있는 경우
    public static <T> ApplicationResponse<T> create(ErrorCode errorCode, T data) {
        return (ApplicationResponse<T>) ApplicationResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(data)
                .build();
    }
}
