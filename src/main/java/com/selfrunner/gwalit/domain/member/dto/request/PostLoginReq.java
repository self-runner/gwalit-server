package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostLoginReq {

    private String phone;

    private String password;

    private String type;
}
