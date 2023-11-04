package com.selfrunner.gwalit.domain.notification.dto.request;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.notification.entity.Notification;
import lombok.Getter;

@Getter
public class NotificationReq {

    private String title;

    private String body;

    public Notification toEntity(Member member) {
        return Notification.builder()
                .memberId(member.getMemberId())
                .title(this.title)
                .body(this.body)
                .build();

    }

}
