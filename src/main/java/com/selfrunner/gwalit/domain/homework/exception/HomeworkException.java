package com.selfrunner.gwalit.domain.homework.exception;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;

public class HomeworkException extends ApplicationException {

    public HomeworkException(ErrorCode errorCode) {
        super(errorCode);
    }
}
