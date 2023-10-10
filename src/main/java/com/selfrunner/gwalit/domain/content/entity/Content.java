package com.selfrunner.gwalit.domain.content.entity;

import com.selfrunner.gwalit.domain.content.dto.request.ContentReq;
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


    @Column(name = "link_url", columnDefinition = "text")
    private String linkUrl;

    @Column(name = "thumbnail_url", columnDefinition = "text")
    private String thumbnailUrl;

    @Column(name = "duration", columnDefinition = "int")
    private Integer duration;

    @Column(name = "is_pinned")
    private Boolean isPinned;

    public void updateVideo(ContentReq contentReq) {
        this.title = contentReq.getTitle();
        this.writer = contentReq.getWriter();
        this.type = ContentType.valueOf(contentReq.getType());
        this.classification = ContentClassification.valueOf(contentReq.getClassification());
        this.linkUrl = contentReq.getLinkUrl();
        this.thumbnailUrl = contentReq.getThumbnailUrl();
        this.duration = contentReq.getDuration();
        this.isPinned = contentReq.getIsPinned();
    }

    public void updateNotion(ContentReq contentReq, String thumbnailUrl) {
        this.title = contentReq.getTitle();
        this.writer = contentReq.getWriter();
        this.type = ContentType.valueOf(contentReq.getType());
        this.classification = ContentClassification.valueOf(contentReq.getClassification());
        this.linkUrl = contentReq.getLinkUrl();
        this.thumbnailUrl = thumbnailUrl;
        this.duration = contentReq.getDuration();
        this.isPinned = contentReq.getIsPinned();
    }

    public void updateIsPinned() {
        this.isPinned = !this.isPinned;
    }

    @Builder
    public Content(String title, String writer, String type, String classification, String linkUrl, String thumbnailUrl, Integer duration, Boolean isPinned) {
        this.title = title;
        this.writer = writer;
        this.type = ContentType.valueOf(type);
        this.classification = ContentClassification.valueOf(classification);
        this.linkUrl = linkUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.isPinned = isPinned;
    }
}
