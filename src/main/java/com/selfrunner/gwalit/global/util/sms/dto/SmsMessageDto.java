package com.selfrunner.gwalit.global.util.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessageDto {
    private String to;
    private String content;
}
