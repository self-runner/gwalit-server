package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PostAuthCodeReq {

    @NotEmpty
    @Size(min = 10, max = 11)
    private String phone;

    private String type;

    @NotBlank
    @Size(min = 6, max = 6)
    private String authorizationCode;
}
