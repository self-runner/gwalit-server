package com.selfrunner.gwalit.domain.content.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selfrunner.gwalit.domain.content.entity.Content;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.sql.Time;

@Getter
@RequiredArgsConstructor
public class PostContentReq {

    @NotEmpty
    private String title;

    @NotEmpty
    private String type;

    @NotEmpty
    private String linkUrl;

    @NotEmpty
    private String thumbnail;

    @NotEmpty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private Time duration;

    public Content toEntity(String title, String type, String linkUrl, String thumbnail, Time duration) {
        return Content.builder()
                .title(title)
                .type(type)
                .linkUrl(linkUrl)
                .thumbnail(thumbnail)
                .duration(duration)
                .build();
    }
}
