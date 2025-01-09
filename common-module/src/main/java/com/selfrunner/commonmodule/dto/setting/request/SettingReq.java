package com.selfrunner.commonmodule.dto.setting.request;

import com.selfrunner.commonmodule.vo.setting.Inform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class SettingReq {

    @NotNull
    private Inform inform;
}
