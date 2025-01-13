package com.selfrunner.domainmodule.domain.lesson.exception;

import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;

public class LessonException extends ApplicationException {

    public LessonException(ErrorCode errorCode) {
        super(errorCode);
    }
}
