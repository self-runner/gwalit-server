package com.selfrunner.domainmodule.domain.lecture.exception;

import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;

public class LectureException extends ApplicationException {

    public LectureException(ErrorCode errorCode) {
        super(errorCode);
    }
}
