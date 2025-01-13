package com.selfrunner.apimodule.application.setting;

import com.selfrunner.commonmodule.dto.setting.request.SettingReq;
import com.selfrunner.commonmodule.dto.setting.response.SettingRes;
import com.selfrunner.commonmodule.dto.setting.response.TosRes;
import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.setting.Setting;
import com.selfrunner.domainmodule.domain.setting.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;

    @Transactional
    public SettingRes register(SettingReq settingReq) {
        // Business Logic
        Setting setting = new Setting(settingReq.getInform());
        settingRepository.save(setting);

        // Response
        return new SettingRes(setting.getSettingId(), setting.getInform());
    }

    public SettingRes getSettingInform() {
        // Business Logic
        Setting setting = settingRepository.findById(1L).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return new SettingRes(setting.getSettingId(), setting.getInform());
    }

    @Transactional
    public SettingRes update(Long settingId, SettingReq settingReq) {
        // Business Logic
        Setting setting = settingRepository.findById(settingId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        setting.update(settingReq.getInform());

        // Response
        return new SettingRes(setting.getSettingId(), setting.getInform());
    }

    @Transactional
    public void delete(Long settingId) {
        // Business Logic
        settingRepository.deleteById(settingId);

        // Response
    }

    public TosRes getTermsOfService() {
        // Business Logic
        Setting setting = settingRepository.findById(3L)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        return new TosRes(setting.getSettingId(), setting.getInform().getNotice(), setting.getInform().getContact());
    }
}
