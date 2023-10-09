package com.selfrunner.gwalit.domain.content.entity;

import com.selfrunner.gwalit.domain.content.dto.request.PutContentReq;
import com.selfrunner.gwalit.domain.member.dto.request.PutMemberReq;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Getter
@Table(name = "Content")
@SQLDelete(sql = "UPDATE content set deleted_at = NOW() where content_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contentId")
    private Long contentId;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "writer", columnDefinition = "varchar(255)")
    private String writer;

    @Column(name = "type", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private ContentType type;

    @Column(name = "classification", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private ContentClassification classification;


    @Column(name = "url_link", columnDefinition = "text")
    private String urlLink;

    @Column(name = "thumbnail_link", columnDefinition = "text")
    private String thumbnailLink;

    @Column(name = "duration", columnDefinition = "int")
    private Integer duration;

    @Column(name = "is_pinned", columnDefinition = "tinyint(1)")
    private Boolean isPinned;

    public void update(Content content) {
        this.title = content.getTitle();
        this.type = content.getType();
        this.urlLink = content.getUrlLink();
        this.thumbnailLink = content.getThumbnailLink();
        this.duration = content.getDuration();
    }

    @Builder
    public Content(String title, String writer, String type, String classification, String urlLink, String thumbnailLink, Integer duration, Boolean isPinned) {
        this.title = title;
        this.writer = writer;
        this.type = ContentType.valueOf(type);
        this.classification = ContentClassification.valueOf(classification);
        this.urlLink = urlLink;
        this.thumbnailLink = thumbnailLink;
        this.duration = duration;
        this.isPinned = isPinned;
    }
}
