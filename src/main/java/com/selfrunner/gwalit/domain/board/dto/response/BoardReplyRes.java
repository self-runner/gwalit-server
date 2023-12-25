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
public class BoardReplyRes {

    private final Long boardId;

    private final Long lectureId;

    private final Long memberId;

    private final MemberType memberType;

    private final String memberName;

    private final Boolean isPublic;

    private Long lessonId;

    private LocalDate lessonDate;

    private final String title;

    private String body;

    private final BoardCategory category;

    private final QuestionStatus status;

    private List<FileRes> fileList;

    private Integer replyCount;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;

    public BoardReplyRes(Board board, Member member, LocalDate lessonDate, Integer replyCount, List<FileRes> fileList) {
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
        this.status = board.getStatus();
        this.fileList = fileList;
        this.replyCount = replyCount;
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
