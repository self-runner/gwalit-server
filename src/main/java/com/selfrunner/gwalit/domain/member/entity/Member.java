package com.selfrunner.gwalit.domain.member.entity;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
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
}
