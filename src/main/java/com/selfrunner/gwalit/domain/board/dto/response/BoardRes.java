package com.selfrunner.gwalit.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.selfrunner.gwalit.domain.board.entity.Board;
import com.selfrunner.gwalit.domain.board.enumerate.QuestionStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardRes {

    private final Long boardId;

    private final Long lectureId;

    private final Long memberId;

    private final Boolean isPublic;

    private final Long lessonId;

    private final String title;

    private final String body;

    private final QuestionStatus status;

    private final List<String> fileList;

    public BoardRes(Board board, List<String> fileList) {
        this.boardId = board.getBoardId();
        this.lectureId = board.getLectureId();
        this.memberId = board.getMemberId();
        this.isPublic = board.getIsPublic();
        this.lessonId = board.getLessonId();
        this.title = board.getTitle();
        this.body = board.getBody();
        this.status =  board.getStatus();
        this.fileList = fileList;
    }
}
