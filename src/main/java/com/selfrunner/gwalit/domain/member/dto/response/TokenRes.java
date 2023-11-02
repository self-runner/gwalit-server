package com.selfrunner.gwalit.domain.member.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenRes {

    private final Long tokenId;

    private final Long memberId;

    private final String token;
}
