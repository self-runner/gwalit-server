package com.selfrunner.gwalit.domain.board.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class PutBoardReq {

    private Long lessonId;

    @Size(min = 1, message = "제목이 입력되어 있어야 합니다.")
    private String title;

    private String body;

    @NotNull
    private String status;

    @NotNull
    private List<String> deleteFileList;
}
