package com.selfrunner.gwalit.domain.lecture.entity;

import com.selfrunner.gwalit.domain.lecture.dto.request.PatchColorReq;
import com.selfrunner.gwalit.domain.lecture.dto.request.PatchNameReq;
import com.selfrunner.gwalit.domain.lecture.dto.request.PutLectureReq;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import com.selfrunner.gwalit.global.common.Schedule;
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
@Table(name = "Lecture")
@TypeDef(name = "json", typeClass = JsonType.class)
@SQLDelete(sql = "UPDATE lecture SET deleted_at = NOW() where lecture_id = ?")
@Where(clause = "deleted_at IS NULL")
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

    @Column(name = "subject")
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @Column(name = "subject_detail")
    private String subjectDetail;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @Type(type = "json")
    @Column(name = "rules", columnDefinition = "json")
    private List<Rule> rules;

    @Type(type = "json")
    @Column(name = "schedules", columnDefinition = "json")
    private List<Schedule> schedules;

    public void update(PutLectureReq putLectureReq) {
        this.name = putLectureReq.getName();
        this.color = putLectureReq.getColor();
        this.subject = Subject.valueOf(putLectureReq.getSubject());
        this.subjectDetail = putLectureReq.getSubjectDetail();;
        this.startDate = putLectureReq.getStartDate();
        this.endDate = putLectureReq.getEndDate();
        this.rules = putLectureReq.getRules();
        this.schedules = putLectureReq.getSchedules();
    }

    public void updateColor(PatchColorReq patchColorReq) {
        this.color = patchColorReq.getColor();
    }

    public void updateName(PatchNameReq patchNameReq) {
        this.name = patchNameReq.getName();
    }

    @Builder
    public Lecture(String name, String color, String subject, String subjectDetail, LocalDate startDate, LocalDate endDate, List<Rule> rules, List<Schedule> schedules) {
        this.name = name;
        this.color = color;
        this.subject = Subject.valueOf(subject);
        this.subjectDetail = subjectDetail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rules = rules;
        this.schedules = schedules;
    }
}
