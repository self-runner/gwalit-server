package com.selfrunner.gwalit.domain.content.dto.response;

import com.selfrunner.gwalit.domain.content.entity.Content;
import com.selfrunner.gwalit.domain.content.entity.ContentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Time;

@Getter
@RequiredArgsConstructor
public class GetContentRes {

    private String contentId;

    private String title;

    private ContentType type;

    private String linkUrl;

    private String thumbnail;

    private Time duration;

    public static GetContentRes toDto(Content content) {
        GetContentRes getContentRes = new GetContentRes();
        getContentRes.contentId = content.getContentId().toString();
        getContentRes.title = content.getTitle();
        getContentRes.type = content.getType();
        getContentRes.linkUrl = content.getLinkUrl();
        getContentRes.thumbnail = content.getThumbnail();
        getContentRes.duration = content.getDuration();

        return getContentRes;
    }
}
