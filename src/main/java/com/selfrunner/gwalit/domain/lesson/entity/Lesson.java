package com.selfrunner.gwalit.domain.lesson.entity;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lesson.dto.request.PatchLessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.dto.request.PutLessonReq;
import com.selfrunner.gwalit.global.common.Day;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    @Column(name = "participants", columnDefinition = "json")
    private List<Participant> participants;

    @Column(name = "feedback", columnDefinition = "text")
    private String feedback;

    @Type(type = "json")
    @Column(name = "progresses", columnDefinition = "json")
    private List<Progress> progresses;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "weekday")
    @Enumerated(EnumType.STRING)
    private Day weekday;

    @Column(name = "startTime", columnDefinition = "time")
    private LocalTime startTime;

    @Column(name = "endTime", columnDefinition = "time")
    private LocalTime endTime;

    public void update(PutLessonReq putLessonReq) {
        this.type = LessonType.valueOf(putLessonReq.getType());
        this.participants = putLessonReq.getParticipants();
        this.feedback = putLessonReq.getFeedback();
        this.progresses = putLessonReq.getProgresses();
        this.date = putLessonReq.getDate();
        this.weekday = putLessonReq.getTime().getWeekday();
        this.startTime = LocalTime.parse(putLessonReq.getTime().getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = LocalTime.parse(putLessonReq.getTime().getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));
    }

    public void updateMeta(PatchLessonMetaRes patchLessonMetaRes) {
        this.participants = patchLessonMetaRes.getParticipants();
        this.date = patchLessonMetaRes.getDate();
        this.weekday = patchLessonMetaRes.getTime().getWeekday();
        this.startTime = LocalTime.parse(patchLessonMetaRes.getTime().getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = LocalTime.parse(patchLessonMetaRes.getTime().getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Builder
    public Lesson(Lecture lecture, String type, List<Participant> participants, String feedback, List<Progress> progresses, LocalDate date, Schedule time) {
        this.lecture = lecture;
        this.type = LessonType.valueOf(type);
        this.participants = participants;
        this.feedback = feedback;
        this.progresses = progresses;
        this.date = date;
        this.weekday = time.getWeekday();
        this.startTime = LocalTime.parse(time.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = LocalTime.parse(time.getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));

    }
}
