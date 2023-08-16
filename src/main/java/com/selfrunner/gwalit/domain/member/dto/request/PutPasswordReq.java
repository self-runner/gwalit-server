package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@RequiredArgsConstructor
public class PutPasswordReq {

    @NotBlank
    private Long memberId;

    private String oldPassword;

    @NotNull
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~@#$%^&*()_\\-\\+=\\[\\]|\\\\;:\\'\",<>\\.?/!])[a-zA-Z\\d~@#$%^&*()_\\-\\+=\\[\\]|\\\\;:\\'\",<>\\.?/!]*$")
    private String newPassword;

    private String newPasswordCheck;
}
