package com.selfrunner.gwalit.domain.lesson.entity;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.global.common.Schedule;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
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
    @Enumerated(EnumType.STRING)
    private LessonType type;

    @Type(type = "json")
    @Column(name = "students", columnDefinition = "json")
    private List<Student> students;

    @Column(name = "feedback")
    private String feedback;

    @Type(type = "json")
    @Column(name = "progresses", columnDefinition = "json")
    private List<Progress> progresses;

    @Column(name = "date")
    private LocalDate date;

    @Type(type = "json")
    @Column(name = "time", columnDefinition = "json")
    private Schedule time;

    @Builder
    public Lesson(Lecture lecture, String type, List<Student> students, String feedback, List<Progress> progresses, LocalDate date, Schedule time) {
        this.lecture = lecture;
        this.type = LessonType.valueOf(type);
        this.students = students;
        this.feedback = feedback;
        this.progresses = progresses;
        this.date = date;
        this.time = time;
    }
}
