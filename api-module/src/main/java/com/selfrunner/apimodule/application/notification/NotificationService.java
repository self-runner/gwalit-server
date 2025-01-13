package com.selfrunner.apimodule.application.notification;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.domainmodule.domain.member.MemberAndNotification;
import com.selfrunner.domainmodule.domain.member.repository.MemberAndNotificationRepository;
import com.selfrunner.domainmodule.domain.member.repository.MemberRepository;
import com.selfrunner.commonmodule.dto.notification.request.NotificationDeepLinkReq;
import com.selfrunner.commonmodule.dto.notification.request.NotificationReq;
import com.selfrunner.commonmodule.dto.notification.response.NotificationRes;
import com.selfrunner.domainmodule.domain.notification.Notification;
import com.selfrunner.domainmodule.domain.notification.repository.NotificationRepository;
import com.selfrunner.notificationmodule.fcm.FCMClient;
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
    public void sendTo(Member member, NotificationDeepLinkReq notificationDeepLinkReq) {
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
        Notification notification = Notification.builder()
                .memberId(notificationDeepLinkReq.getMemberId())
                .title(notificationDeepLinkReq.getTitle())
                .body(notificationDeepLinkReq.getBody())
                .name(notificationDeepLinkReq.getName())
                .lectureId(notificationDeepLinkReq.getLectureId())
                .lessonId(notificationDeepLinkReq.getLessonId())
                .url(notificationDeepLinkReq.getUrl())
                .build();
        Notification saveNotification = notificationRepository.save(notification);
        MemberAndNotification memberAndNotification = MemberAndNotification.builder()
                .memberId(notificationDeepLinkReq.getMemberId())
                .notificationId(saveNotification.getNotificationId())
                .build();
        memberAndNotificationRepository.save(memberAndNotification);

        // Response
    }

    @Transactional
    public NotificationRes sendMulticast(Member member, NotificationReq notificationReq) {
        // Validation
        // TODO: 관리자 권한 확인 필요

        // Business Logic
        Notification notification = Notification.builder()
                .title(notificationReq.getTitle())
                .body(notificationReq.getBody())
                .name("notice")
                .url(notificationReq.getUrl())
                .build();
        Notification saveNotification = notificationRepository.save(notification);
        List<String> tokenList = memberRepository.findTokenList();
        //FCMMessageDto fcmMessageDto = FCMMessageDto.toDto(saveNotification);
        MulticastMessage multicastMessage = fcmClient.makeMulticastMessage(tokenList, saveNotification.getTitle(), saveNotification.getBody(), saveNotification.getName(), saveNotification.getLectureId(), saveNotification.getLessonId(), saveNotification.getDate(), saveNotification.getUrl(), saveNotification.getBoardId());
        if(!tokenList.isEmpty()) {
            fcmClient.sendMulticast(tokenList, multicastMessage);
        }
        else {
            throw new ApplicationException(ErrorCode.USER_LIST_EMPTY);
        }

        // Response
        return new NotificationRes(saveNotification.getNotificationId(),
                saveNotification.getMemberId(),
                saveNotification.getTitle(),
                saveNotification.getBody(),
                saveNotification.getName(),
                saveNotification.getLectureId(),
                saveNotification.getLessonId(),
                saveNotification.getUrl(),
                saveNotification.getBoardId(),
                saveNotification.getCreatedAt());
    }

    public Slice<NotificationRes> getNotificationList(Member member, Long cursor, Pageable pageable) {
        // Validation

        // Business Logic
        Notification notification = (cursor != null)
                ? notificationRepository.findById(cursor).orElse(null)
                : null;

        // Response
        return (notification != null)
                ? notificationRepository.findNotificationPageableBy(cursor, notification.getCreatedAt(), pageable, member.getMemberId())
                : notificationRepository.findNotificationPageableBy(cursor, null, pageable, member.getMemberId());
    }

}
