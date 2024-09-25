package com.selfrunner.gwalit.domain.member.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum AuthorizationType {

    SIGNUP("회원가입"),
    PASSWORD_RESET("비밀번호 재설정");

    private final String description;
}
