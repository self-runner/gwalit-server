package com.selfrunner.gwalit.domain.banner.dto.request;

import com.selfrunner.gwalit.domain.banner.entity.Banner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@RequiredArgsConstructor
public class BannerReq {

    @NotNull
    @Pattern(regexp = "(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")
    private String linkUrl;

    public Banner toEntity(String imageUrl) {
        Banner banner = Banner.builder()
                .imageUrl(imageUrl)
                .linkUrl(this.linkUrl)
                .build();

        return banner;
    }
}
