package com.selfrunner.gwalit.domain.banner.dto.response;

import com.selfrunner.gwalit.domain.banner.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BannerRes {

    private Long bannerId;

    private String imageUrl;

    private String linkUrl;

    private String information;

    public BannerRes(Banner banner) {
        this.bannerId = banner.getBannerId();
        this.imageUrl = banner.getImageUrl();
        this.linkUrl = banner.getLinkUrl();
        this.information = banner.getInformation();
    }
}
