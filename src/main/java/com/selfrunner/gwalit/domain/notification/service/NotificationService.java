package com.selfrunner.gwalit.domain.notification.service;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.notification.dto.request.NotificationReq;
import com.selfrunner.gwalit.domain.notification.dto.response.NotificationRes;
import com.selfrunner.gwalit.domain.notification.entity.Notification;
import com.selfrunner.gwalit.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationRes sendAll(Long version, Member member, NotificationReq notificationReq) {
        // Validation

        // Business Logic

        // Response
        return null;
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
