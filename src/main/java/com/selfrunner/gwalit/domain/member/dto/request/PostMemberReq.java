package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostMemberReq {

    private String name;

    private String type;

    private String phone;

    private String password;

    private String school;

    private String grade;
}
