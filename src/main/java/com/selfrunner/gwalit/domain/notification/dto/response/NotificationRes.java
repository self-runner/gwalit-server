package com.selfrunner.gwalit.domain.notification.dto.response;

import com.selfrunner.gwalit.domain.notification.entity.Notification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NotificationRes {

    private final Long notificationId;

    private final Long memberId;

    private final String title;

    private final String body;

    private final String name;

    private final Long lectureId;

    private final Long lessonId;

    private final String url;

    private final LocalDateTime createdAt;

    public NotificationRes(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.memberId = notification.getMemberId();
        this.title = notification.getTitle();
        this.body = notification.getBody();
        this.name = notification.getName();
        this.lectureId = notification.getLectureId();
        this.lessonId = notification.getLessonId();
        this.url = notification.getUrl();
        this.createdAt = notification.getCreatedAt();
    }
}
