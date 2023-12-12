package com.selfrunner.gwalit.domain.board.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileRes {

    private final String name;

    private final String url;

    private final Long size;
}
