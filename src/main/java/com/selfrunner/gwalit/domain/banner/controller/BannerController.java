package com.selfrunner.gwalit.domain.banner.controller;

import com.selfrunner.gwalit.domain.banner.service.BannerService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Null;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banner")
@Tag(name = "Banner", description = "메인 페이지 배너 관련")
public class BannerController {

    private final BannerService bannerService;

    @PostMapping("/")
    public ApplicationResponse<Null> register() {
        bannerService.register();
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }

}
