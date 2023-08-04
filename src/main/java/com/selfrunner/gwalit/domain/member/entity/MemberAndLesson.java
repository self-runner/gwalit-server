package com.selfrunner.gwalit.domain.member.entity;

import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "MemberAndLesson")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAndLesson extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberAndLessonId")
    private Long memberAndLessonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lessonId")
    private Lesson lesson;

    @Column(name = "isTeacher")
    private Boolean isTeacher;

    @Builder
    public MemberAndLesson(Member member, Lesson lesson) {
        this.member = member;
        this.lesson = lesson;
        this.isTeacher = (member.getType().equals(MemberType.TEACHER)) ? Boolean.TRUE : Boolean.FALSE;
    }
}
