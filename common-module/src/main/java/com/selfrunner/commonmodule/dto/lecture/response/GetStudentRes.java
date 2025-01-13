package com.selfrunner.commonmodule.dto.lecture.response;

import com.selfrunner.commonmodule.enumerate.member.MemberGrade;
import com.selfrunner.commonmodule.enumerate.member.MemberState;
import com.selfrunner.commonmodule.enumerate.member.MemberType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetStudentRes {

    private final Long memberId;

    private final String name;

    private final MemberType type;

    private final MemberState state;

    private final String phone;

    private final String school;

    private final MemberGrade grade;
}
