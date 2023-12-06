package com.selfrunner.gwalit.domain.question.exception;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;

public class QuestionException extends ApplicationException {

    public QuestionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
