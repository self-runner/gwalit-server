package com.selfrunner.gwalit.domain.notification.entity;

import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "name", columnDefinition = "varchar(255)") // 딥링크용 네임
    private String name;

    @Column(name = "lecture_id", columnDefinition = "bigint") // 딥링크용 클래스 ID
    private Long lectureId;

    @Column(name = "lesson_id", columnDefinition = "bigint") // 딥링크용 수업 리포트 ID
    private Long lessonId;

    @Column(name = "date", columnDefinition = "date")
    private LocalDate date;

    @Column(name = "url", columnDefinition = "text")
    private String url;

    @Builder
    public Notification(Long memberId, String title, String body, String name, Long lectureId, Long lessonId, LocalDate date, String url) {
        this.memberId = memberId;
        this.title = title;
        this.body = body;
        this.name = name;
        this.lectureId = lectureId;
        this.lessonId = lessonId;
        this.date = date;
        this.url = url;
    }
}
