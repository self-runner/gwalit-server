package com.selfrunner.gwalit.domain.notification.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.notification.dto.response.NotificationRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<NotificationRes> findNotificationPageableBy(Long cursor, LocalDateTime cursorCreatedAt, Pageable pageable, Long memberId) {
        return null;
    }
}
