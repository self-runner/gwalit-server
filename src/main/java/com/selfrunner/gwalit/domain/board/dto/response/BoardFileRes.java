package com.selfrunner.gwalit.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardFileRes {

    private Long boardId;

    private Long fileCapacity;
}
