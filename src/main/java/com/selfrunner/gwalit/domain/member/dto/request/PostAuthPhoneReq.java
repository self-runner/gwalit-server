package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PostAuthPhoneReq {

    @NotEmpty
    @Size(min = 10, max = 11)
    private String phone;
}
