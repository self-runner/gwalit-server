package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostAuthCodeReq {

    private String phone;

    private String type;

    private String authorizationCode;
}
