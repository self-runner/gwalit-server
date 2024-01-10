package com.selfrunner.gwalit.domain.board.exception;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;

public class BoardException extends ApplicationException {

    public BoardException(ErrorCode errorCode) {
        super(errorCode);
    }
}
