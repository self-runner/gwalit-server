package com.selfrunner.gwalit.domain.banner.dto.response;

import com.selfrunner.gwalit.domain.banner.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BannerRes {

    private String bannerId;

    private String imageUrl;

    private String linkUrl;

    public BannerRes(Banner banner) {
        this.bannerId = banner.getBannerId().toString();
        this.imageUrl = banner.getImageUrl();
        this.linkUrl = banner.getLinkUrl();
    }
}
