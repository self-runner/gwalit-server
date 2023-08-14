package com.selfrunner.gwalit.domain.setting.controller;

import com.selfrunner.gwalit.domain.setting.service.SettingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "설정", description = "설정 페이지 관련 API")
public class SettingController {

    private final SettingService settingService;
}
