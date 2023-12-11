package com.selfrunner.gwalit.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.selfrunner.gwalit.domain.board.enumerate.QuestionStatus;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
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

    private final QuestionStatus status;

    private final Integer replyCount;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;
}
