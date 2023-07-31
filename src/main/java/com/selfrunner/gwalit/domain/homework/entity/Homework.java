package com.selfrunner.gwalit.domain.homework.entity;

import com.selfrunner.gwalit.domain.homework.dto.request.PostHomeworkReq;
import com.selfrunner.gwalit.domain.homework.dto.request.PutHomeworkReq;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
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

    public void update(PutHomeworkReq putHomeworkReq) {
        this.body = putHomeworkReq.getBody();
        this.deadline = putHomeworkReq.getDeadline();
        this.isFinish = putHomeworkReq.getIsFinish();
    }

    @Builder
    public Homework(String lessonId, Long memberId, String body, LocalDate deadline, Boolean isFinish) {
        this.lessonId = (lessonId.equals(null)) ? -1L : Long.valueOf(lessonId);
        this.memberId = memberId;
        this.body = body;
        this.deadline = deadline;
        this.isFinish = isFinish;
    }
}
