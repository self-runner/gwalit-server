package com.selfrunner.gwalit.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.selfrunner.gwalit.domain.board.entity.Board;
import com.selfrunner.gwalit.domain.board.enumerate.BoardCategory;
import com.selfrunner.gwalit.domain.board.enumerate.QuestionStatus;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardRes {

    private final Long boardId;

    private final Long lectureId;

    private final Long memberId;

    private final MemberType memberType;

    private final String memberName;

    private final Boolean isPublic;

    private final Long lessonId;

    private final LocalDate lessonDate;

    private final String title;

    private final String body;

    private final BoardCategory category;

    private final QuestionStatus status;

    private final List<FileRes> fileList;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public BoardRes(Board board, Member member, LocalDate lessonDate, List<FileRes> fileList) {
        this.boardId = board.getBoardId();
        this.lectureId = board.getLecture().getLectureId();
        this.memberId = member.getMemberId();
        this.memberType = member.getType();
        this.memberName = member.getName();
        this.isPublic = board.getIsPublic();
        this.lessonId = board.getLessonId();
        this.lessonDate = lessonDate;
        this.title = board.getTitle();
        this.body = board.getBody();
        this.category = board.getCategory();
        this.status =  board.getStatus();
        this.fileList = fileList;
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
