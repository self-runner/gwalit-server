package com.selfrunner.gwalit.domain.board.dto.request;

import com.selfrunner.gwalit.domain.board.entity.Board;
import com.selfrunner.gwalit.domain.board.entity.Reply;
import com.selfrunner.gwalit.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class ReplyReq {

    private String body;

    public Reply toEntity(Board board, Member member) {
        return Reply.builder()
                .board(board)
                .member(member)
                .body(this.body)
                .build();
    }
}
