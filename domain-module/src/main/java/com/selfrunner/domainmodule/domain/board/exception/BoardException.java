package com.selfrunner.domainmodule.domain.board.exception;

import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;

public class BoardException extends ApplicationException {

    public BoardException(ErrorCode errorCode) {
        super(errorCode);
    }
}
