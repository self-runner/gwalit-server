package com.selfrunner.gwalit.domain.member.exception;

import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;

public class MemberException extends ApplicationException {

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
