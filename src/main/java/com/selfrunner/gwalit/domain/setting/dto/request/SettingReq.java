package com.selfrunner.gwalit.domain.setting.dto.request;

import com.selfrunner.gwalit.domain.setting.entity.Inform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class SettingReq {

    @NotNull
    private Inform inform;
}
