package com.selfrunner.gwalit.domain.homework.entity;

import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkReq;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Table(name = "Homework")
@SQLDelete(sql = "UPDATE homework SET deleted_at = NOW() where homework_id = ?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Homework extends BaseTimeEntity {

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

    public void update(HomeworkReq homeworkReq) {
        this.body = homeworkReq.getBody();
        this.deadline = homeworkReq.getDeadline();
        this.isFinish = homeworkReq.getIsFinish();
    }

    public boolean isSameHomework(HomeworkReq homeworkReq) {
        return Objects.equals(body, homeworkReq.getBody()) &&
                Objects.equals(deadline, homeworkReq.getDeadline()) &&
                Objects.equals(isFinish, homeworkReq.getIsFinish());
    }

    @Builder
    public Homework(Long lessonId, Long memberId, String body, LocalDate deadline, Boolean isFinish) {
        this.lessonId = lessonId;
        this.memberId = memberId;
        this.body = body;
        this.deadline = deadline;
        this.isFinish = isFinish;
    }
}
