package com.selfrunner.gwalit.domain.log.dto;

import com.selfrunner.gwalit.domain.log.entity.Log;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LogReq {

    private Long memberId;

    private String type;

    private String endPoint;

    private Boolean isSuccess;

    public Log toEntity() {
        Log log = Log.builder()
                .memberId(this.memberId)
                .type(this.type)
                .endPoint(this.endPoint)
                .isSuccess(this.isSuccess)
                .build();

        return log;
    }

}
