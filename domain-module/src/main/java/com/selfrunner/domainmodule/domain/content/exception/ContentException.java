package com.selfrunner.domainmodule.domain.content.exception;


import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;

public class ContentException extends ApplicationException {

    public ContentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
