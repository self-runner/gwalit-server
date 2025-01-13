package com.selfrunner.apimodule.presentation.setting;

import com.selfrunner.commonmodule.common.ApplicationResponse;
import com.selfrunner.commonmodule.dto.setting.request.SettingReq;
import com.selfrunner.commonmodule.dto.setting.response.SettingRes;
import com.selfrunner.commonmodule.dto.setting.response.TosRes;
import com.selfrunner.apimodule.application.setting.SettingService;
import com.selfrunner.commonmodule.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/setting")
@Tag(name = "설정", description = "설정 페이지 관련 API")
public class SettingController {

    private final SettingService settingService;

    @Operation(description = "설정 정보 등록 API")
    @PostMapping("")
    public ApplicationResponse<SettingRes> register(@Valid @RequestBody SettingReq settingReq) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, settingService.register(settingReq));
    }

    @Operation(description = "설정 정보 수정 API")
    @PutMapping("/{setting_id}")
    public ApplicationResponse<SettingRes> update(@PathVariable("setting_id") Long settingId, @Valid @RequestBody SettingReq settingReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, settingService.update(settingId, settingReq));
    }

    @Operation(description = "설정 정보 삭제 API")
    @DeleteMapping("/{setting_id}")
    public ApplicationResponse<Void> delete(@PathVariable("setting_id") Long settingId) {
        settingService.delete(settingId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(description = "설정 정보 반환 API")
    @GetMapping("")
    public ApplicationResponse<SettingRes> getSettingInform() {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, settingService.getSettingInform());
    }

    @Operation(description = "이용 약관 주소 반환 API")
    @GetMapping("/tos")
    public ApplicationResponse<TosRes> getTermsOfService() {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, settingService.getTermsOfService());
    }
}
