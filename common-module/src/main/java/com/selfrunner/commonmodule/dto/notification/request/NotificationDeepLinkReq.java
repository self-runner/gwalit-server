package com.selfrunner.commonmodule.dto.notification.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class NotificationDeepLinkReq {

    private Long memberId;

    private String title;

    private String body;

    private String name;

    private Long lectureId;

    private Long lessonId;

    private LocalDate date;

    private String url;
}
