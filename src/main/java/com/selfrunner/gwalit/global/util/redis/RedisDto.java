package com.selfrunner.gwalit.global.util.redis;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RedisDto {

    private String key; // Redis Key
    private String value; // Redis Value
    private boolean isSuccess; // Redis 작업 성공 여부
}
