package com.selfrunner.gwalit.domain.notification.controller;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.notification.dto.request.NotificationDeepLinkReq;
import com.selfrunner.gwalit.domain.notification.dto.request.NotificationReq;
import com.selfrunner.gwalit.domain.notification.dto.response.NotificationRes;
import com.selfrunner.gwalit.domain.notification.service.NotificationService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
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
@RequestMapping("/api")
@Tag(name = "Notification Controller", description = "FCM을 이용한 알람 API")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(description = "단일 발송")
    @PostMapping("/v{version}/notification")
    public ApplicationResponse<NotificationRes> sendTo(@PathVariable("version") Long version, @Auth Member member, @Valid @RequestBody NotificationDeepLinkReq notificationDeepLinkReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, notificationService.sendTo(version, member, notificationDeepLinkReq));
    }
  
    @Operation(description = "전체 발송 (알림/광고 등)")
    @PostMapping("/v{version}/notification/all")
    public ApplicationResponse<NotificationRes> sendAll(@PathVariable("version") Long version, @Auth Member member, @Valid @RequestBody NotificationReq notificationReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, notificationService.sendAll(version, member, notificationReq));
    }

    @Operation(description = "알림 리스트 조회하기")
    @GetMapping("/v{version}/notification/list")
    public ApplicationResponse<Slice<NotificationRes>> getNotificationList(@PathVariable("version") Long version, @Auth Member member, @RequestParam("cursor") Long cursor, @PageableDefault(size = 30, sort = "") Pageable pageable) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, notificationService.getNotificationList(version, member, cursor, pageable));
    }
}
