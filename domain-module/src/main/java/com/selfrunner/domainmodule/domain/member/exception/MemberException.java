package com.selfrunner.domainmodule.domain.member.exception;

import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;

public class MemberException extends ApplicationException {

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
