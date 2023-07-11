package com.selfrunner.gwalit.domain.member.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetRefreshRes {
    private String accessToken;

    public GetRefreshRes toDto(String accessToken) {
        GetRefreshRes getRefreshRes = new GetRefreshRes();
        getRefreshRes.accessToken = accessToken;

        return getRefreshRes;
    }
}
