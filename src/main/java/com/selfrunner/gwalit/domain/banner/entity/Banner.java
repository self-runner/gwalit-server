package com.selfrunner.gwalit.domain.banner.entity;

import com.selfrunner.gwalit.domain.banner.dto.request.BannerReq;
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

    @Column(name = "type", columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private BannerType type;

    @Column(name = "imageUrl", columnDefinition = "text")
    private String imageUrl;

    @Column(name = "linkUrl", columnDefinition = "text")
    private String linkUrl;

    @Column(name = "information", columnDefinition = "varchar(255)")
    private String information;

    @Column(name = "priority", columnDefinition = "int")
    private Integer priority;

    public void updateImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void update(BannerReq bannerReq) {
        this.type = BannerType.valueOf(bannerReq.getType());
        this.linkUrl = bannerReq.getLinkUrl();
        this.information = bannerReq.getInformation();
        this.priority = bannerReq.getPriority();
    }

    @Builder
    public Banner(String imageUrl, String type, String linkUrl, String information, Integer priority) {
        this.imageUrl = imageUrl;
        this.type = BannerType.valueOf(type);
        this.linkUrl = linkUrl;
        this.information = information;
        this.priority = priority;
    }
}
