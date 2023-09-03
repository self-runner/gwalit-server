package com.selfrunner.gwalit.domain.content.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selfrunner.gwalit.domain.content.entity.Content;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Time;

@Getter
@RequiredArgsConstructor
public class ContentReq {

    @NotEmpty
    private String title;

    @NotEmpty
    private String type;

    @NotEmpty
    @Pattern(regexp = "(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")
    private String linkUrl;

    @NotEmpty
    @Pattern(regexp = "(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)")
    private String thumbnail;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private Time duration;

    public Content toEntity() {
        return Content.builder()
                .title(this.title)
                .type(this.type)
                .linkUrl(this.linkUrl)
                .thumbnail(this.thumbnail)
                .duration(this.duration)
                .build();
    }
}
