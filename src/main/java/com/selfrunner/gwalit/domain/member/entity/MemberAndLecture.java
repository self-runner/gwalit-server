package com.selfrunner.gwalit.domain.member.entity;

import com.selfrunner.gwalit.domain.lecture.dto.request.PatchColorReq;
import com.selfrunner.gwalit.domain.lecture.dto.request.PatchNameReq;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "MemberAndLecture")
@SQLDelete(sql = "UPDATE member_and_lecture SET deleted_at = NOW() where member_and_lecture_id = ?")
@Where(clause = "deleted_at IS NULL")
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

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "is_update") // 학생이 업데이트한 적이 있는지 확인하는 문구
    private Boolean isUpdate;

    public void updateName(PatchNameReq patchNameReq) {
        this.name = patchNameReq.getName();
    }

    public void updateColor(PatchColorReq patchColorReq) {
        this.color = patchColorReq.getColor();
    }

    @Builder
    public MemberAndLecture(Member member, Lecture lecture) {
        this.member = member;
        this.lecture = lecture;
        this.isTeacher = (member.getType().equals(MemberType.TEACHER)) ? Boolean.TRUE : Boolean.FALSE;
        this.name = lecture.getName();
        this.color = lecture.getColor();
        this.isUpdate = Boolean.FALSE;
    }
}
