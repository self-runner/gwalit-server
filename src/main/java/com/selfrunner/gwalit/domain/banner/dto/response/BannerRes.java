package com.selfrunner.gwalit.domain.banner.dto.response;

import com.selfrunner.gwalit.domain.banner.entity.Banner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BannerRes {

    private String imageUrl;

    private String linkUrl;

    public BannerRes toDto(Banner banner) {
        BannerRes bannerRes = new BannerRes();
        bannerRes.imageUrl = banner.getImageUrl();
        bannerRes.linkUrl = banner.getLinkUrl();

        return bannerRes;
    }
}
