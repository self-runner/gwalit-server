package com.selfrunner.gwalit.domain.member.entity;

import com.selfrunner.gwalit.global.util.SHA256;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

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


    public void encryptPassword(String password) {
        this.password = SHA256.encrypt(password);
    }

    @Builder
    public Member(String name, String type, String phone, String password, String school, String grade) {
        this.name = name;
        this.type = MemberType.valueOf(type);
        this.phone = phone;
        this.password = password;
        this.school = school;
        this.grade = MemberGrade.valueOf(grade);
        this.needNotification = Boolean.FALSE;
    }
}
