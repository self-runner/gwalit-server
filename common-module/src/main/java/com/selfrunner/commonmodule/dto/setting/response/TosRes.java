package com.selfrunner.commonmodule.dto.setting.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TosRes {

    private final Long settingId;

    private final String tos;

    private final String marketing;
}
