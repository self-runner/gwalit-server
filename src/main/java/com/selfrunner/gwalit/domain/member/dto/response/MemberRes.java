package com.selfrunner.gwalit.domain.member.dto.response;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberGrade;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberRes {
    private Long memberId;

    private String name;

    private MemberType type;

    private String phone;

    private String school;

    private MemberGrade grade;

    private Boolean needNotification;

    private Boolean isAdvertisement;

    private Boolean isPrivacy;

    public MemberRes toDto(Member member) {
        MemberRes memberRes = new MemberRes();
        memberRes.memberId = member.getMemberId();
        memberRes.name = member.getName();
        memberRes.type = member.getType();
        memberRes.phone = member.getPhone();
        memberRes.school = member.getSchool();
        memberRes.grade = member.getGrade();
        memberRes.needNotification = member.getNeedNotification();
        memberRes.isAdvertisement = member.getIsAdvertisement();
        memberRes.isPrivacy = member.getIsPrivacy();

        return memberRes;
    }

}
