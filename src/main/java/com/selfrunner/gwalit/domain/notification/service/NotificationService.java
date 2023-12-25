package com.selfrunner.gwalit.domain.notification.service;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndNotification;
import com.selfrunner.gwalit.domain.member.repository.MemberAndNotificationRepository;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.domain.notification.dto.request.NotificationDeepLinkReq;
import com.selfrunner.gwalit.domain.notification.dto.request.NotificationReq;
import com.selfrunner.gwalit.domain.notification.dto.response.NotificationRes;
import com.selfrunner.gwalit.domain.notification.entity.Notification;
import com.selfrunner.gwalit.domain.notification.repository.NotificationRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.fcm.FCMClient;
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
    private final MemberAndNotificationRepository memberAndNotificationRepository;
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
        //FCMMessageDto fcmMessageDto = FCMMessageDto.toDto(m.getToken(), notificationDeepLinkReq.getTitle(), notificationDeepLinkReq.getBody(), notificationDeepLinkReq.getName(), notificationDeepLinkReq.getLectureId(), notificationDeepLinkReq.getLessonId(), notificationDeepLinkReq.getDate(), notificationDeepLinkReq.getUrl());
        Message message = fcmClient.makeMessage(m.getToken(), notificationDeepLinkReq.getTitle(), notificationDeepLinkReq.getBody(), notificationDeepLinkReq.getName(), notificationDeepLinkReq.getLectureId(), notificationDeepLinkReq.getLessonId(), notificationDeepLinkReq.getDate(), notificationDeepLinkReq.getUrl(), null);
        fcmClient.send(message);
        Notification notification = notificationDeepLinkReq.toEntity();
        Notification saveNotification = notificationRepository.save(notification);
        MemberAndNotification memberAndNotification = MemberAndNotification.builder()
                .memberId(notificationDeepLinkReq.getMemberId())
                .notificationId(saveNotification.getNotificationId())
                .build();
        memberAndNotificationRepository.save(memberAndNotification);

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
        List<String> tokenList = memberRepository.findTokenList();
        //FCMMessageDto fcmMessageDto = FCMMessageDto.toDto(saveNotification);
        MulticastMessage multicastMessage = fcmClient.makeMulticastMessage(tokenList, saveNotification);
        if(!tokenList.isEmpty()) {
            fcmClient.sendMulticast(tokenList, multicastMessage);
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
        Notification notification = (cursor != null) ? notificationRepository.findById(cursor).orElse(null) : null;
        Slice<NotificationRes> notificationResSlice = (notification != null) ? notificationRepository.findNotificationPageableBy(cursor, notification.getCreatedAt(), pageable, member.getMemberId()) : notificationRepository.findNotificationPageableBy(cursor, null, pageable, member.getMemberId());

        // Response
        return notificationResSlice;
    }

}
