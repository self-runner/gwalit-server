package com.selfrunner.commonmodule.dto.content.response;

import com.selfrunner.commonmodule.enumerate.content.ContentClassification;
import com.selfrunner.commonmodule.enumerate.content.ContentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ContentRes {

    private final Long contentId;

    private final String title;

    private final String writer;

    private final ContentType type;

    private final ContentClassification classification;

    private final String linkUrl;

    private final String thumbnailUrl;

    private final Integer duration;

    private final Boolean isPinned;
}
