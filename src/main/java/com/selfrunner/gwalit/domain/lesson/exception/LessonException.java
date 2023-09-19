package com.selfrunner.gwalit.domain.lesson.exception;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;

public class LessonException extends ApplicationException {

    public LessonException(ErrorCode errorCode) {
        super(errorCode);
    }
}
