package com.selfrunner.gwalit.domain.member.entity;

import com.selfrunner.gwalit.domain.member.dto.request.PutMemberReq;
import com.selfrunner.gwalit.global.common.BaseTimeEntity;
import com.selfrunner.gwalit.global.util.SHA256;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Member")
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() where member_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long memberId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MemberType type;

    @Column(name = "phone")
    private String phone;
    @Column(name = "password")
    private String password;

    @Column(name = "school")
    private String school;

    @Column(name = "grade")
    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    @Column(name = "needNotification")
    private Boolean needNotification;

    @Column(name = "isAdvertisement")
    private Boolean isAdvertisement;

    @Column(name = "isPrivacy")
    private Boolean isPrivacy;


    public void encryptPassword(String password) {
        this.password = SHA256.encrypt(password);
        this.needNotification = Boolean.FALSE;
    }

    public void setNeedNotification() {
        this.needNotification = Boolean.TRUE;
    }

    public void update(PutMemberReq putMemberReq) {
        this.name = putMemberReq.getName();
        this.school = putMemberReq.getSchool();
        this.grade = MemberGrade.valueOf(putMemberReq.getGrade());
        this.isAdvertisement = putMemberReq.getIsAdvertisement();
        this.isPrivacy = putMemberReq.getIsPrivacy();
    }

    @Builder
    public Member(String name, String type, String phone, String password, String school, String grade, Boolean isAdvertisement, Boolean isPrivacy) {
        this.name = name;
        this.type = MemberType.valueOf(type);
        this.phone = phone;
        this.password = password;
        this.school = school;
        this.grade = MemberGrade.valueOf(grade);
        this.needNotification = Boolean.FALSE;
        this.isAdvertisement = isAdvertisement;
        this.isPrivacy = isPrivacy;
    }
}
