package com.selfrunner.gwalit.domain.workbook.exception;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;

public class WorkbookException extends ApplicationException {

    public WorkbookException(ErrorCode errorCode) {
        super(errorCode);
    }
}
