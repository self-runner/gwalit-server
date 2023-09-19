package com.selfrunner.gwalit.domain.task.exception;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;

public class TaskException extends ApplicationException {

    public TaskException(ErrorCode errorCode) {
        super(errorCode);
    }
}
