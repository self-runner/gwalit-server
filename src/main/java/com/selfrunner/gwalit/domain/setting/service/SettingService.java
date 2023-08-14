package com.selfrunner.gwalit.domain.setting.service;

import com.selfrunner.gwalit.domain.setting.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;
}
