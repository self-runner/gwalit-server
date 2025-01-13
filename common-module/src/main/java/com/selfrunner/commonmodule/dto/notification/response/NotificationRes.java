package com.selfrunner.commonmodule.dto.notification.response;

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

    private final Long boardId;

    private final LocalDateTime createdAt;
}
