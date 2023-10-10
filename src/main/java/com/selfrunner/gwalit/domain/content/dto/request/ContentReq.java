package com.selfrunner.gwalit.domain.content.dto.request;

import com.selfrunner.gwalit.domain.content.entity.Content;
import com.selfrunner.gwalit.domain.content.entity.ContentType;
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

    public Content toEntity(String thumbnailUrl) {
        if(ContentType.valueOf(this.type).equals(ContentType.NOTION)) {
            return Content.builder()
                    .title(this.title)
                    .writer(this.writer)
                    .type(this.type)
                    .classification(this.classification)
                    .linkUrl(this.linkUrl)
                    .thumbnailUrl(thumbnailUrl)
                    .duration(this.duration)
                    .isPinned(this.isPinned)
                    .build();
        }

        return Content.builder()
                .title(this.title)
                .writer(this.writer)
                .type(this.type)
                .classification(this.classification)
                .linkUrl(this.linkUrl)
                .thumbnailUrl(this.thumbnailUrl)
                .duration(this.duration)
                .isPinned(this.isPinned)
                .build();
    }
}
