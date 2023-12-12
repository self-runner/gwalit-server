package com.selfrunner.gwalit.domain.board.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.selfrunner.gwalit.domain.board.entity.Reply;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
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

    public ReplyRes(Reply reply, Member member, List<FileRes> fileUrlList) {
        this.replyId = reply.getReplyId();
        this.boardId = reply.getBoard().getBoardId();
        this.memberId = member.getMemberId();
        this.memberType = member.getType();
        this.memberName = member.getName();
        this.body = reply.getBody();
        this.fileUrlList = fileUrlList;
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
    }
}
