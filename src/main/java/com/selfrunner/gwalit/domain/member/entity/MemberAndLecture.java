package com.selfrunner.gwalit.domain.member.entity;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "MemberAndLectrue")
@SQLDelete(sql = "UPDATE member_and_lectrue SET deleted_at = new() where member_and_lecture_id = ?")
public class MemberAndLecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberAndLectureId")
    private Long memberAndLectureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lectureId")
    private Lecture lecture;

    @Column(name = "isTeacher")
    private Boolean isTeacher;
}
