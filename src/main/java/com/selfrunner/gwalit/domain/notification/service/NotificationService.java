package com.selfrunner.gwalit.domain.notification.service;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.domain.notification.dto.request.NotificationDeepLinkReq;
import com.selfrunner.gwalit.domain.notification.dto.request.NotificationReq;
import com.selfrunner.gwalit.domain.notification.dto.response.NotificationRes;
import com.selfrunner.gwalit.domain.notification.entity.Notification;
import com.selfrunner.gwalit.domain.notification.repository.NotificationRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.fcm.FCMClient;
import com.selfrunner.gwalit.global.util.fcm.dto.FCMMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final FCMClient fcmClient;

    @Transactional
    public NotificationRes sendTo(Long version, Member member, NotificationDeepLinkReq notificationDeepLinkReq) {
        // Validation
        // TODO: 관리자 권한 확인 필요

        // Business Logic
        Member m = memberRepository.findById(notificationDeepLinkReq.getMemberId()).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(m.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
        }
        FCMMessageDto fcmMessageDto = FCMMessageDto.toDto(m.getToken(), notificationDeepLinkReq.getTitle(), notificationDeepLinkReq.getBody(), notificationDeepLinkReq.getName(), notificationDeepLinkReq.getLectureId(), notificationDeepLinkReq.getLessonId(), notificationDeepLinkReq.getDate(), notificationDeepLinkReq.getUrl());
        fcmClient.send(fcmMessageDto);

        // Response
        return null;
    }

    @Transactional
    public NotificationRes sendMulticast(Long version, Member member, NotificationReq notificationReq) {
        // Validation
        // TODO: 관리자 권한 확인 필요

        // Business Logic
        Notification notification = notificationReq.toEntity();
        Notification saveNotification = notificationRepository.save(notification);
        FCMMessageDto fcmMessageDto = FCMMessageDto.toDto(saveNotification);
        List<String> tokenList = memberRepository.findTokenList();;
        if(!tokenList.isEmpty()) {
            fcmClient.sendMulticast(tokenList, fcmMessageDto);
        }
        else {
            throw new ApplicationException(ErrorCode.USER_LIST_EMPTY);
        }

        // Response
        return new NotificationRes(saveNotification);
    }

    public Slice<NotificationRes> getNotificationList(Long version, Member member, Long cursor, Pageable pageable) {
        // Validation

        // Business Logic
        Notification notification = notificationRepository.findById(cursor).orElse(null);
        Slice<NotificationRes> notificationResSlice = (notification != null) ? notificationRepository.findNotificationPageableBy(cursor, notification.getCreatedAt(), pageable, member.getMemberId()) : notificationRepository.findNotificationPageableBy(cursor, null, pageable, member.getMemberId());

        // Response
        return null;
    }

}
