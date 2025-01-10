package com.selfrunner.commonmodule.dto.content.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
public class ContentReq {

    @NotEmpty
    private String title;

    @NotEmpty
    private String writer;

    @NotEmpty
    private String type;

    @NotEmpty
    private String classification;

    @NotEmpty
    @Pattern(regexp = "(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")
    private String linkUrl;

    @Pattern(regexp = "(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")
    private String thumbnailUrl;

    @NotNull
    private Integer duration;

    @NotNull
    private Boolean isPinned;
}
