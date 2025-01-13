package com.selfrunner.apimodule.presentation.notification;

import com.selfrunner.apimodule.global.auth.Auth;
import com.selfrunner.commonmodule.common.ApplicationResponse;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.commonmodule.dto.notification.request.NotificationDeepLinkReq;
import com.selfrunner.commonmodule.dto.notification.request.NotificationReq;
import com.selfrunner.commonmodule.dto.notification.response.NotificationRes;
import com.selfrunner.apimodule.application.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Tag(name = "Notification Controller", description = "FCM을 이용한 알람 API")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(description = "단일 발송")
    @PostMapping("")
    public ApplicationResponse<Void> sendTo(@Auth Member member, @Valid @RequestBody NotificationDeepLinkReq notificationDeepLinkReq) {
        notificationService.sendTo(member, notificationDeepLinkReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }
  
    @Operation(description = "전체 발송 (알림/광고 등)")
    @PostMapping("/all")
    public ApplicationResponse<NotificationRes> sendMulticast(@Auth Member member, @Valid @RequestBody NotificationReq notificationReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, notificationService.sendMulticast(member, notificationReq));
    }

    @Operation(description = "알림 리스트 조회하기")
    @GetMapping("/list")
    public ApplicationResponse<Slice<NotificationRes>> getNotificationList(@Auth Member member, @RequestParam(value = "cursor", required = false) Long cursor, @PageableDefault(size = 30, sort = "created_at DESC") Pageable pageable) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, notificationService.getNotificationList(member, cursor, pageable));
    }
}
