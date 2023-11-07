package com.selfrunner.gwalit.domain.notification.dto.request;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.notification.entity.Notification;
import lombok.Getter;

@Getter
public class NotificationReq {

    private String title;

    private String body;

    private String url;

    public Notification toEntity() {
        return Notification.builder()
                .title(this.title)
                .body(this.body)
                .name("notice")
                .url(this.url)
                .build();

    }

}
