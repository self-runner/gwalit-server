package com.selfrunner.commonmodule.dto.board.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
//@Builder
public class PostBoardReq {

    @NotNull
    private Long lectureId;

    @NotNull
    private Boolean isPublic;

    private Long lessonId;

    @Size(min = 1, message = "제목이 입력되어 있어야 합니다.")
    private String title;

    private String body;

    @NotNull
    private String category;

    @NotNull
    private String status;
}
