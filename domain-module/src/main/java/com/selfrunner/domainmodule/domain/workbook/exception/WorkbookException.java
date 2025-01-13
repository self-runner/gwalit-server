package com.selfrunner.domainmodule.domain.workbook.exception;

import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;

public class WorkbookException extends ApplicationException {

    public WorkbookException(ErrorCode errorCode) {
        super(errorCode);
    }
}
