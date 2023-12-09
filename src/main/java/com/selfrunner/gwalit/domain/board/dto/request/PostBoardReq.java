package com.selfrunner.gwalit.domain.board.dto.request;

import com.selfrunner.gwalit.domain.board.entity.Board;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
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
    private String status;

    public Board toEntity(Long memberId) {
        return Board.builder()
                .lectureId(this.lectureId)
                .memberId(memberId)
                .isPublic(this.isPublic)
                .lessonId(this.lessonId)
                .title(this.title)
                .body(this.body)
                .status(this.status)
                .build();
    }
}
