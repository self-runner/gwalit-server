package com.selfrunner.gwalit.domain.lecture.entity;

import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Map;

@Entity
@Getter
@Table(name = "Lecture")
@TypeDef(name = "json", typeClass = JsonType.class)
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

    @Type(type = "json")
    @Column(name = "schedules", columnDefinition = "longtext")
    private Map<String, Object> schedules;
}
