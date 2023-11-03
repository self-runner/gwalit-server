package com.selfrunner.gwalit.domain.member.entity;

import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "member_and_notification")
@SQLDelete(sql = "UPDATE member_and_notification SET deleted_at = NOW() where member_and_notification_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAndNotification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_and_notification_id")
    private Long memberAndNotificationId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "notification_id")
    private Long notificationId;

    @Builder
    public MemberAndNotification(Long memberId, Long notificationId) {
        this.memberId = memberId;
        this.notificationId = notificationId;
    }
}
