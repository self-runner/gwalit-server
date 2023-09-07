package com.selfrunner.gwalit.domain.lecture.dto.response;

import com.selfrunner.gwalit.domain.member.entity.MemberGrade;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
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
