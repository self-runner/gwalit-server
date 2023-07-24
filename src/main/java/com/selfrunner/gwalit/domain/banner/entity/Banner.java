package com.selfrunner.gwalit.domain.banner.entity;

import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Banner")
@SQLDelete(sql = "UPDATE banner SET deleted_at = NOW() where banner_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bannerId")
    private Long bannerId;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "linkUrl")
    private String linkUrl;
}
