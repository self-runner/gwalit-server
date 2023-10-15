package com.selfrunner.gwalit.domain.notification.controller;

import com.selfrunner.gwalit.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Tag(name = "Notification Controller", description = "FCM을 이용한 알람 API")
public class NotificationController {

    private final NotificationService notificationService;
}
