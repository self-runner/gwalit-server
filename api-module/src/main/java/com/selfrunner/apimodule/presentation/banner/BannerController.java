package com.selfrunner.apimodule.presentation.banner;

import com.selfrunner.apimodule.application.banner.BannerService;
import com.selfrunner.commonmodule.common.ApplicationResponse;
import com.selfrunner.commonmodule.dto.banner.request.BannerReq;
import com.selfrunner.commonmodule.dto.banner.response.BannerRes;
import com.selfrunner.commonmodule.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/banner")
@Tag(name = "Banner", description = "메인 페이지 배너 관련")
public class BannerController {

    private final BannerService bannerService;

    @PostMapping("")
    public ApplicationResponse<BannerRes> register(@Valid @RequestPart(value = "data") BannerReq bannerReq, @RequestPart(value = "file") MultipartFile multipartFile) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, bannerService.register(bannerReq, multipartFile));
    }

    @PutMapping("/{banner_id}")
    public ApplicationResponse<BannerRes> update(@PathVariable("banner_id") Long bannerId, @Valid @RequestPart(value = "data") BannerReq bannerReq, @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, bannerService.update(bannerId, bannerReq, multipartFile));
    }

    @GetMapping("")
    public ApplicationResponse<List<BannerRes>> getAll() {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, bannerService.getAll());
    }

    @GetMapping("/content")
    public ApplicationResponse<List<BannerRes>> getContent() {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, bannerService.getContent());
    }

    @DeleteMapping("/{banner_id}")
    public ApplicationResponse<Void> delete(@PathVariable("banner_id") Long bannerId) {
        bannerService.delete(bannerId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }
}
