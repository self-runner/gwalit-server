package com.selfrunner.gwalit.domain.question.entity;

import com.selfrunner.gwalit.domain.question.enumerate.QuestionStatus;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;


@Entity
@Getter
@Table(name = "question")
@SQLDelete(sql = "")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id", columnDefinition = "bigint")
    private Long questionId;

    @Column(name = "lecture_id", columnDefinition = "bigint")
    private Long lectureId;

    @Column(name = "member_id", columnDefinition = "bigint")
    private Long memberId;

    @Column(name = "is_public", columnDefinition = "tinyint(1)")
    private Boolean isPublic;

    @Column(name = "lesson_id", columnDefinition = "bigint")
    private Long lessonId;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Column(name = "status", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private QuestionStatus status;

    @Builder
    public Question(Long lectureId, Long memberId, Boolean isPublic, Long lessonId, String title, String body, String status) {
        this.lectureId = lectureId;
        this.memberId = memberId;
        this.isPublic = isPublic;
        this.lessonId = lessonId;
        this.title = title;
        this.body = body;
        this.status = QuestionStatus.valueOf(status);
    }
}
