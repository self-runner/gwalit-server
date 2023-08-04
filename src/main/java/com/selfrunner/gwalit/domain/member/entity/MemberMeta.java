package com.selfrunner.gwalit.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberMeta {

    private final Long memberId;

    private final String name;

    private final Boolean isTeacher;
}
