package com.selfrunner.gwalit.domain.banner.controller;

import com.selfrunner.gwalit.domain.banner.dto.request.BannerReq;
import com.selfrunner.gwalit.domain.banner.dto.response.BannerRes;
import com.selfrunner.gwalit.domain.banner.service.BannerService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Null;

@RestController
@RequiredArgsConstructor
@RequestMapping("/banner")
@Tag(name = "Banner", description = "메인 페이지 배너 관련")
public class BannerController {

    private final BannerService bannerService;

    @PostMapping("/")
    public ApplicationResponse<BannerRes> register(@Valid @RequestPart(value = "data") BannerReq bannerReq, @RequestPart(value = "file") MultipartFile multipartFile) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, bannerService.register(bannerReq, multipartFile));
    }

}
