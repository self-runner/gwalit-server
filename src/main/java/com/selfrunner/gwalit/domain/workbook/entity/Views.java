package com.selfrunner.gwalit.domain.workbook.entity;

import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "VIEWS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Views {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "views_id")
    private Long viewsId;

    @Column(name = "count", columnDefinition = "bigint")
    private Long count;
}
