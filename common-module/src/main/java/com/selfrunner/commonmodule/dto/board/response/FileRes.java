package com.selfrunner.commonmodule.dto.board.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileRes {

    private final String name;

    private final String url;

    private final Long size;
}
