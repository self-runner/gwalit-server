package com.selfrunner.commonmodule.dto.setting.response;

import com.selfrunner.commonmodule.vo.setting.Inform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SettingRes {

    private final Long settingId;

    private final Inform inform;
}
