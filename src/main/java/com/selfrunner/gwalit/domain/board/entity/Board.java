package com.selfrunner.gwalit.domain.board.entity;

import com.selfrunner.gwalit.domain.board.dto.request.PutBoardReq;
import com.selfrunner.gwalit.domain.board.enumerate.BoardCategory;
import com.selfrunner.gwalit.domain.board.enumerate.QuestionStatus;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;


@Entity
@Getter
@Table(name = "board")
@SQLDelete(sql = "UPDATE board SET deleted_at = NOW() where board_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", columnDefinition = "bigint")
    private Long boardId;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "lesson_id", columnDefinition = "bigint")
    private Long lessonId;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Column(name = "category", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private BoardCategory category;

    @Column(name = "status", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private QuestionStatus status;

    public void update(PutBoardReq putBoardReq) {
        this.lessonId = putBoardReq.getLessonId();
        this.title = putBoardReq.getTitle();
        this.body = putBoardReq.getBody();
        this.status = QuestionStatus.valueOf(putBoardReq.getStatus());
    }

    public void changeQuestionStatus() {
        if(this.status.equals(QuestionStatus.SOLVED)) {
            this.status = QuestionStatus.UNSOLVED;
        }
        else if(this.status.equals(QuestionStatus.UNSOLVED)) {
            this.status = QuestionStatus.SOLVED;
        }
    }

    @Builder
    public Board(Lecture lecture, Member member, Boolean isPublic, Long lessonId, String title, String body, String category, String status) {
        this.lecture = lecture;
        this.member = member;
        this.isPublic = isPublic;
        this.lessonId = lessonId;
        this.title = title;
        this.body = body;
        this.category = BoardCategory.valueOf(category);
        this.status = QuestionStatus.valueOf(status);
    }
}
