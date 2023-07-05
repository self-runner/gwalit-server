package com.selfrunner.gwalit.domain.member.entity;

import io.swagger.v3.oas.annotations.Operation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long memberId;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "type")
    private MemberType type;

    @NotNull
    @Column(name = "phone")
    private String phone;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "school")
    private String school;

    @Column(name = "grade")
    private MemberGrade grade;
}
