package com.selfrunner.gwalit.domain.content.dto.response;

import com.selfrunner.gwalit.domain.content.entity.Content;
import com.selfrunner.gwalit.domain.content.entity.ContentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Time;

@Getter
@RequiredArgsConstructor
public class ContentRes {

    private String contentId;

    private String title;

    private ContentType type;

    private String linkUrl;

    private String thumbnail;

    private Time duration;

    public ContentRes(Content content) {
        this.contentId = content.getContentId().toString();
        this.title = content.getTitle();
        this.type = content.getType();
        this.linkUrl = content.getLinkUrl();
        this.thumbnail = content.getThumbnail();
        this.duration = content.getDuration();
    }
}
