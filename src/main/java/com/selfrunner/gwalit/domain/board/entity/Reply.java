package com.selfrunner.gwalit.domain.board.entity;

import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "reply")
@SQLDelete(sql = "")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id", columnDefinition = "bigint")
    private Long replyId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Board board;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Builder
    public Reply(Board board, String body) {
        this.board = board;
        this.body = body;
    }
}
