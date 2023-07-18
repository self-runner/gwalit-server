package com.selfrunner.gwalit.domain.lecture.entity;

import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Lecture")
@SQLDelete(sql = "UPDATE lecture SET deleted_at = NOW() where lecture_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lectureId")
    private Long lectureId;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    @Column(name = "rules")
    private String rules;

    @Column(name = "schedules")
    private String schedules;
}
