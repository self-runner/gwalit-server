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

    public ContentRes toDto(Content content) {
        ContentRes contentRes = new ContentRes();
        contentRes.contentId = content.getContentId().toString();
        contentRes.title = content.getTitle();
        contentRes.type = content.getType();
        contentRes.linkUrl = content.getLinkUrl();
        contentRes.thumbnail = content.getThumbnail();
        contentRes.duration = content.getDuration();

        return contentRes;
    }

    public static ContentRes staticToDto(Content content) {
        ContentRes contentRes = new ContentRes();
        contentRes.contentId = content.getContentId().toString();
        contentRes.title = content.getTitle();
        contentRes.type = content.getType();
        contentRes.linkUrl = content.getLinkUrl();
        contentRes.thumbnail = content.getThumbnail();
        contentRes.duration = content.getDuration();

        return contentRes;
    }
}
