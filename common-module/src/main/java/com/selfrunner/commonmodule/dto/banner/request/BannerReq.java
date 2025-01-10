package com.selfrunner.commonmodule.dto.banner.request;

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

    @NotNull
    private String type;

    private String information;

    @NotNull
    private Integer priority;
}
