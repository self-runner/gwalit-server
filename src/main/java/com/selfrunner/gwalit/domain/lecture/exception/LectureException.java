package com.selfrunner.gwalit.domain.lecture.exception;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;

public class LectureException extends ApplicationException {

    public LectureException(ErrorCode errorCode) {
        super(errorCode);
    }
}
