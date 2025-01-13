package com.selfrunner.domainmodule.domain.task.exception;

import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;

public class TaskException extends ApplicationException {

    public TaskException(ErrorCode errorCode) {
        super(errorCode);
    }
}
