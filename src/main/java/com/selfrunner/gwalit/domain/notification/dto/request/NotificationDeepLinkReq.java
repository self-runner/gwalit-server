package com.selfrunner.gwalit.domain.notification.dto.request;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.notification.entity.Notification;
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
