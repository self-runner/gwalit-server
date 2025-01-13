package com.selfrunner.commonmodule.dto.banner.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BannerRes {

    private Long bannerId;

    private String imageUrl;

    private String linkUrl;

    private String information;

    public BannerRes(Long bannerId, String imageUrl, String linkUrl, String information) {
        this.bannerId = bannerId;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.information = information;
    }
}
