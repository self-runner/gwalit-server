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
@Table(name = "file")
@SQLDelete(sql = "")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", columnDefinition = "bigint")
    private Long fileId;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "url", columnDefinition = "text")
    private String url;

    @Column(name = "size", columnDefinition = "bigint")
    private Long size;

    @Column(name = "member_id", columnDefinition = "bigint")
    private Long memberId;

    @Column(name = "lecture_id", columnDefinition = "bigint")
    private Long lectureId;

    private Long boardId;

    private Long replyId;

    @Builder
    public File(String name, String url, Long size, Long memberId, Long lectureId, Long boardId, Long replyId) {
        this.name = name;
        this.url = url;
        this.size = size;
        this.memberId = memberId;
        this.lectureId = lectureId;
        this.boardId = boardId;
        this.replyId = replyId;
    }
}
