package com.selfrunner.gwalit.domain.banner.entity;

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
@Table(name = "Banner")
@SQLDelete(sql = "UPDATE banner SET deleted_at = NOW() where banner_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bannerId")
    private Long bannerId;

    @Column(name = "imageUrl", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "linkUrl", columnDefinition = "text")
    private String linkUrl;

    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateLink(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Builder
    public Banner(String imageUrl, String linkUrl) {
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
    }
}
