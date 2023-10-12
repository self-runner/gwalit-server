package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PostAuthPhoneReq {

    @NotNull(message = "전화번호가 Null입니다.")
    @Size(min = 9, max = 12, message = "유효한 전화번호 길이가 아닙니다.")
    @Pattern(regexp = "[0-9]+", message = "숫자만 입력되어야 합니다.")
    private String phone;
}
