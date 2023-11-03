package com.selfrunner.gwalit.domain.member.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetRefreshRes {

    private String accessToken;

    private String refreshToken;

    public GetRefreshRes toDto(String accessToken, String refreshToken) {
        GetRefreshRes getRefreshRes = new GetRefreshRes();
        getRefreshRes.accessToken = accessToken;
        getRefreshRes.refreshToken = refreshToken;

        return getRefreshRes;
    }
}
