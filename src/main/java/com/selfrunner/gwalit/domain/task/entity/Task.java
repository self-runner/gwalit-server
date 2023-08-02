package com.selfrunner.gwalit.domain.task.entity;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
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
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Table(name = "Subtask")
@TypeDef(name = "json", typeClass = JsonType.class)
@SQLDelete(sql = "UPDATE task SET deleted_at = NOW() where task_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId")
    private Long taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lectureId")
    private Lecture lecture;

    @Column(name = "title")
    private String title;

    @Column(name = "deadline", columnDefinition = "date")
    private LocalDate deadline;

    @Type(type = "json")
    @Column(name = "subtasks", columnDefinition = "json")
    private List<Subtask> subtasks;
}