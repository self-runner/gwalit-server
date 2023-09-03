package com.selfrunner.gwalit.domain.setting.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TosRes {

    private final Long settingId;

    private final String tos;

    private final String marketing;
}
