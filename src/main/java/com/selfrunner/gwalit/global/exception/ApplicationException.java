package com.selfrunner.gwalit.global.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{
    private final ErrorCode errorCode;

    public ApplicationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
