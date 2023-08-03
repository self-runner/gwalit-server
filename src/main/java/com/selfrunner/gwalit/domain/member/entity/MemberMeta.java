package com.selfrunner.gwalit.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberMeta {

    private Long memberId;

    private String name;

    private Boolean isTeacher;
}
