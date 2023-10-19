package com.selfrunner.gwalit.domain.content.exception;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;

public class ContentException extends ApplicationException {

    public ContentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
