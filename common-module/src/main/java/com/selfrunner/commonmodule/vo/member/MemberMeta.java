package com.selfrunner.commonmodule.vo.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberMeta {

    private final Long memberId;

    private final String name;

    private final Boolean isTeacher;
}
