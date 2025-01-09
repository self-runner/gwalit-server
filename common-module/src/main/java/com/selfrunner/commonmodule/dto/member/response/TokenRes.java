package com.selfrunner.commonmodule.dto.member.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenRes {

    private final Long memberId;

    private final String token;
}
