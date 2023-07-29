package com.selfrunner.gwalit.domain.lesson.entity;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lecture.entity.Schedule;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Table(name = "Lesson")
@TypeDef(name = "json", typeClass = JsonType.class)
@SQLDelete(sql = "UPDATE lesson SET deleted_at = NOW() where lesson_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lesson extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lessonId")
    private Long lessonId;

    @ManyToOne
    @JoinColumn(name = "lectureId")
    private Lecture lecture;

    @Column(name = "type")
    private LessonType type;

    @Type(type = "json")
    @Column(name = "students", columnDefinition = "json")
    private List<Student> students;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "date")
    private LocalDate date;

    @Type(type = "json")
    @Column(name = "startTime", columnDefinition = "json")
    private Schedule startTime;

    @Type(type = "json")
    @Column(name = "endTime", columnDefinition = "json")
    private Schedule endTime;

}
