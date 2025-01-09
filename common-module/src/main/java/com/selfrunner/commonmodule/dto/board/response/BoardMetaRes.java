package com.selfrunner.commonmodule.dto.board.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.selfrunner.commonmodule.enumerate.board.BoardCategory;
import com.selfrunner.commonmodule.enumerate.board.QuestionStatus;
import com.selfrunner.commonmodule.enumerate.member.MemberType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardMetaRes {

    private final Long boardId;

    private final Long lectureId;

    private final Long memberId;

    private final MemberType memberType;

    private final String memberName;

    private final Long lessonId;

    private final String title;

    private final String body;

    private final BoardCategory category;

    private final QuestionStatus status;

    private final Long replyCount;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;
}
