package com.selfrunner.gwalit.domain.member.entity;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "MemberAndLecture")
@SQLDelete(sql = "UPDATE member_and_lectrue SET deleted_at = new() where member_and_lecture_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public MemberAndLecture(Member member, Lecture lecture) {
        this.member = member;
        this.lecture = lecture;
        this.isTeacher = (member.getType().equals(MemberType.TEACHER) ? Boolean.TRUE : Boolean.FALSE;
    }
}
