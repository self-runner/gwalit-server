package com.selfrunner.domainmodule.domain.member;

import com.selfrunner.commonmodule.dto.lecture.request.PatchColorReq;
import com.selfrunner.commonmodule.dto.lecture.request.PatchNameReq;
import com.selfrunner.domainmodule.common.BaseTimeEntity;
import com.selfrunner.domainmodule.domain.lecture.Lecture;
import com.selfrunner.commonmodule.enumerate.member.MemberType;
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

    public void updateIsUpdate() {
        this.isUpdate = Boolean.TRUE; // 한 번이라도 학생이 변경 시도하면 Boolean.TRUE 값 고정되어야 함.
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
