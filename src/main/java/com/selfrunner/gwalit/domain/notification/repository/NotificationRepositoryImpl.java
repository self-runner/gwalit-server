package com.selfrunner.gwalit.domain.notification.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.notification.dto.response.NotificationRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.selfrunner.gwalit.domain.member.entity.QMemberAndNotification.memberAndNotification;
import static com.selfrunner.gwalit.domain.notification.entity.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<NotificationRes> findNotificationPageableBy(Long cursor, LocalDateTime cursorCreatedAt, Pageable pageable, Long memberId) {
        List<NotificationRes> content = queryFactory.select(Projections.constructor(NotificationRes.class, notification.notificationId, notification.memberId, notification.title, notification.body, notification.name, notification.lectureId, notification.lessonId, notification.createdAt ))
                .from(notification)
                .leftJoin(memberAndNotification).on(memberAndNotification.notificationId.eq(notification.notificationId))
                .where(notification.deletedAt.isNull().and(notification.memberId.isNull().or(memberAndNotification.memberId.eq(memberId))), eqCursorIdAndCursorCreatedAt(cursor, cursorCreatedAt))
                .orderBy(notification.createdAt.desc(), notification.notificationId.asc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if(content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression eqCursorIdAndCursorCreatedAt(Long cursorId, LocalDateTime cursorCreatedAt) {
        if(cursorId == null || cursorCreatedAt == null) {
            return null;
        }

        return notification.createdAt.lt(cursorCreatedAt)
                .or(notification.notificationId.gt(cursorId)
                        .and(notification.createdAt.eq(cursorCreatedAt)));
    }
}
