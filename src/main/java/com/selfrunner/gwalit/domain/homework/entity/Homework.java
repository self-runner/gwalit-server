package com.selfrunner.gwalit.domain.homework.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "Homework")
@SQLDelete(sql = "UPDATE homework SET deleted_at = NOW() where homework_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "homeworkId")
    private Long homeworkId;

    @Column(name = "lessonId")
    private Long lessonId;

    @Column(name = "memberId")
    private Long memberId;

    @Column(name = "body")
    private String body;

    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "isFinish")
    private Boolean isFinish;
}
