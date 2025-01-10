package com.selfrunner.domainmodule.domain.homework.exception;

import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;

public class HomeworkException extends ApplicationException {

    public HomeworkException(ErrorCode errorCode) {
        super(errorCode);
    }
}
