package com.selfrunner.gwalit.global.util.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmsNaverReq {
    private String type;
    private String from;
    private String Content;
    private List<SmsMessageDto> messages;
}
