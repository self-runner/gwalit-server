package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PostAuthCodeReq {

    @NotNull(message = "전화번호가 Null입니다.")
    @Size(min = 10, max = 11, message = "유효한 전화번호 길이가 아닙니다.")
    private String phone;

    /**
     * 임시 비밀번호 발급 요청 시 사용, 최초 회원가입 시 해당 자료형 null로 적용
     **/
    private String type;

    @NotNull(message = "인증번호가 Null입니다.")
    @Size(min = 6, max = 6, message = "인증번호가 6자리가 아닙니다.")
    private String authorizationCode;
}
