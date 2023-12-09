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

    private String name;

    private String fileUrl;

    private Long questionId;

    private Long replyId;

    @Builder
    public File(String name, String fileUrl, Long questionId, Long replyId) {
        this.name = name;
        this.fileUrl = fileUrl;
        this.questionId = questionId;
        this.replyId = replyId;
    }
}
