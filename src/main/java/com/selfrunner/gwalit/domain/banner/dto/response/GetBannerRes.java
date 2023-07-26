package com.selfrunner.gwalit.domain.banner.dto.response;

import com.selfrunner.gwalit.domain.banner.entity.Banner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetBannerRes {

    private Long bannerId;

    private String imageUrl;

    private String linkUrl;

    public GetBannerRes toDto(Banner banner) {
        GetBannerRes getBannerRes = new GetBannerRes();
        getBannerRes.bannerId = banner.getBannerId();
        getBannerRes.imageUrl = banner.getImageUrl();
        getBannerRes.linkUrl = banner.getLinkUrl();

        return getBannerRes;
    }
}
