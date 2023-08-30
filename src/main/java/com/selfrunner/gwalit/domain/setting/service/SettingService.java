package com.selfrunner.gwalit.domain.setting.service;

import com.selfrunner.gwalit.domain.setting.dto.request.SettingReq;
import com.selfrunner.gwalit.domain.setting.dto.response.SettingRes;
import com.selfrunner.gwalit.domain.setting.dto.response.TosRes;
import com.selfrunner.gwalit.domain.setting.entity.Setting;
import com.selfrunner.gwalit.domain.setting.repository.SettingRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
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
        SettingRes settingRes = new SettingRes(setting.getSettingId(), setting.getInform());
        return settingRes;
    }

    public SettingRes getSettingInform() {
        // Business Logic
        Setting setting = settingRepository.findById(1L).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        SettingRes settingRes = new SettingRes(setting.getSettingId(), setting.getInform());
        return settingRes;
    }

    @Transactional
    public SettingRes update(Long settingId, SettingReq settingReq) {
        // Business Logic
        Setting setting = settingRepository.findById(settingId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        setting.update(settingReq.getInform());

        // Response
        SettingRes settingRes = new SettingRes(setting.getSettingId(), setting.getInform());
        return settingRes;
    }

    @Transactional
    public Void delete(Long settingId) {
        // Business Logic
        settingRepository.deleteById(settingId);

        // Response
        return null;
    }

    public TosRes getTermsOfService() {
        // Business Logic
        Setting setting = settingRepository.findById(3L).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Response
        TosRes tosRes= new TosRes(setting.getSettingId(), setting.getInform().getNotice(), setting.getInform().getContact());
        return tosRes;
    }
}
