package com.selfrunner.gwalit.domain.content.dto.response;

import com.selfrunner.gwalit.domain.content.entity.Content;
import com.selfrunner.gwalit.domain.content.entity.ContentClassification;
import com.selfrunner.gwalit.domain.content.entity.ContentType;
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

    public ContentRes(Content content) {
        this.contentId = content.getContentId();
        this.title = content.getTitle();
        this.writer = content.getWriter();
        this.type = content.getType();
        this.classification = content.getClassification();
        this.linkUrl = content.getLinkUrl();
        this.thumbnailUrl = content.getThumbnailUrl();
        this.duration = content.getDuration();
        this.isPinned = content.getIsPinned();
    }
}
