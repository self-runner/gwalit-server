package com.selfrunner.commonmodule.dto.board.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.selfrunner.commonmodule.enumerate.board.BoardCategory;
import com.selfrunner.commonmodule.enumerate.board.QuestionStatus;
import com.selfrunner.commonmodule.enumerate.member.MemberType;
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

    public BoardReplyRes(Long boardId,
                         Long lectureId,
                         Long memberId,
                         MemberType memberType,
                         String memberName,
                         Boolean isPublic,
                         Long lessonId,
                         String boardTitle,
                         String boardBody,
                         BoardCategory category,
                         QuestionStatus status,
                         LocalDate lessonDate,
                         Integer replyCount,
                         List<FileRes> fileList,
                         LocalDateTime createdAt,
                         LocalDateTime modifiedAt) {
        this.boardId = boardId;
        this.lectureId = lectureId;
        this.memberId = memberId;
        this.memberType = memberType;
        this.memberName = memberName;
        this.isPublic = isPublic;
        this.lessonId = lessonId;
        this.lessonDate = lessonDate;
        this.title = boardTitle;
        this.body = boardBody;
        this.category = category;
        this.status = status;
        this.fileList = fileList;
        this.replyCount = (replyCount != null) ? replyCount : 0;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
