package com.selfrunner.gwalit.domain.member.dto.response;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberGrade;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PutMemberRes {

    private Long memberId;

    private String name;

    private String school;

    private MemberGrade grade;

    private Boolean isAdvertisement;

    private Boolean isPrivacy;

    public PutMemberRes toDto(Member member) {
        PutMemberRes putMemberRes = new PutMemberRes();
        putMemberRes.memberId = member.getMemberId();
        putMemberRes.name = member.getName();
        putMemberRes.school = member.getSchool();
        putMemberRes.grade = member.getGrade();
        putMemberRes.isAdvertisement = member.getIsAdvertisement();
        putMemberRes.isPrivacy = member.getIsPrivacy();

        return putMemberRes;
    }
}