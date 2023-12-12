package com.selfrunner.gwalit.domain.board.entity;

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
@Table(name = "reply")
@SQLDelete(sql = "UPDATE reply SET deleted_at = NOW() where reply_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id", columnDefinition = "bigint")
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "body", columnDefinition = "text")
    private String body;

    @Builder
    public Reply(Board board, Member member, String body) {
        this.board = board;
        this.member = member;
        this.body = body;
    }
}
