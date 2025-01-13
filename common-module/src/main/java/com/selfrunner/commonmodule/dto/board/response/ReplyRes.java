package com.selfrunner.commonmodule.dto.board.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.selfrunner.commonmodule.enumerate.member.MemberType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplyRes {

    private final Long replyId;

    private final Long boardId;

    private final Long memberId;

    private final MemberType memberType;

    private final String memberName;

    private final String body;

    private final List<FileRes> fileUrlList;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;
}
