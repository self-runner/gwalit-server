package com.selfrunner.domainmodule.domain.notification.repository;

import com.selfrunner.commonmodule.dto.notification.response.NotificationRes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface NotificationRepositoryCustom {

    Slice<NotificationRes> findNotificationPageableBy(Long cursor, LocalDateTime cursorCreatedAt, Pageable pageable, Long memberId);
}
