package com.selfrunner.gwalit.domain.setting.dto.response;

import com.selfrunner.gwalit.domain.setting.entity.Inform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SettingRes {

    private final Long settingId;

    private final Inform inform;
}
