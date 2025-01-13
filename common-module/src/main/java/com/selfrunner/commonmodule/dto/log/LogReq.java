package com.selfrunner.commonmodule.dto.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LogReq {

    private Long memberId;

    private String type;

    private String endPoint;

    private Boolean isSuccess;
}
