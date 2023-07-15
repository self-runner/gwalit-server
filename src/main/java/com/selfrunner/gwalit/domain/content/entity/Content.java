package com.selfrunner.gwalit.domain.content.entity;

import com.selfrunner.gwalit.domain.content.dto.request.PutContentReq;
import com.selfrunner.gwalit.domain.member.dto.request.PutMemberReq;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Getter
@Table(name = "Content")
@SQLDelete(sql = "UPDATE content set deleted_at = NOW() where content_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contentId")
    private Long contentId;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ContentType type;

    @Column(name = "linkUrl")
    private String linkUrl;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "duration")
    private Time duration;

    public void update(Content content) {
        this.title = content.getTitle();
        this.type = content.getType();
        this.linkUrl = content.getLinkUrl();
        this.thumbnail = content.getThumbnail();
        this.duration = content.getDuration();
    }

    @Builder
    public Content(String title, String type, String linkUrl, String thumbnail, Time duration) {
        this.title = title;
        this.type = ContentType.valueOf(type);
        this.linkUrl = linkUrl;
        this.thumbnail = thumbnail;
        this.duration = duration;
    }
}
