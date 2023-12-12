package com.selfrunner.gwalit.domain.board.dto.request;

import com.selfrunner.gwalit.domain.board.entity.Board;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.member.entity.Member;
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
    private String category;

    @NotNull
    private String status;

    public Board toEntity(Lecture lecture, Member member) {
        return Board.builder()
                .lecture(lecture)
                .member(member)
                .isPublic(this.isPublic)
                .lessonId(this.lessonId)
                .title(this.title)
                .body(this.body)
                .category(this.category)
                .status(this.status)
                .build();
    }
}
