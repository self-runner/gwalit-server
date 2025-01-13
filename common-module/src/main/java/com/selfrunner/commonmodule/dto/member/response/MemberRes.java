package com.selfrunner.commonmodule.dto.member.response;

import com.selfrunner.commonmodule.enumerate.member.MemberGrade;
import com.selfrunner.commonmodule.enumerate.member.MemberType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberRes {
    private final Long memberId;

    private final String name;

    private final MemberType type;

    private final String phone;

    private final String school;

    private final MemberGrade grade;

    private final Boolean needNotification;

    private final Boolean isAdvertisement;

    private final Boolean isPrivacy;
}
