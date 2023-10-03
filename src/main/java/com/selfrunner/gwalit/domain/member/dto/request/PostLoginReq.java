package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PostLoginReq {

    @NotNull(message = "전화번호가 Null입니다.")
    @Size(min = 9, max = 12, message = "유효한 전화번호 길이가 아닙니다.")
    @Pattern(regexp = "[0-9]+", message = "숫자만 입력되어야 합니다.")
    private String phone;

    @NotNull(message = "비밀번호가 Null입니다.")
    @Size(min = 8, max = 20, message = "비밀번호 길이는 8자 이상 20자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~@#$%^&*()_\\-\\+=\\[\\]|\\\\;:\\'\",<>\\.?/!])[a-zA-Z\\d~@#$%^&*()_\\-\\+=\\[\\]|\\\\;:\\'\",<>\\.?/!]*$", message = "비밀번호가 규칙에 맞지 않습니다.")
    private String password;

    @NotEmpty(message = "사용자 유형이 지정되지 않았습니다.")
    private String type;
}
