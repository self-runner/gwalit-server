package com.selfrunner.gwalit.domain.notification.entity;

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
@Table(name = "Notification")
@SQLDelete(sql = "UPDATE notification SET deleted_at = NOW() where notification_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", columnDefinition = "bigint")
    private Long notificationId;

    @Column(name = "member_id", columnDefinition = "bigint")
    private Long memberId;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Builder
    public Notification(Long memberId, String title, String body) {
        this.memberId = memberId;
        this.title = title;
        this.body = body;
    }
}
