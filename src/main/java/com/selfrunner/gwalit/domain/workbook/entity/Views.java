package com.selfrunner.gwalit.domain.workbook.entity;

import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "VIEWS")
@SQLDelete(sql = "UPDATE views set deleted_at = NOW() where views_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Views extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "views_id")
    private Long viewsId;

    @Column(name = "count", columnDefinition = "int default 0")
    private Integer count;

    public void update() {
        this.count++;
    }

    @Builder
    public Views(Integer count) {
        this.count = count;
    }
}
