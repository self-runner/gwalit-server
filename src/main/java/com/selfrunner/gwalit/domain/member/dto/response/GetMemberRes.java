package com.selfrunner.gwalit.domain.member.dto.response;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberGrade;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetMemberRes {
    private Long memberId;

    private String name;

    private MemberType type;

    private String phone;

    private MemberGrade grade;

    private Boolean needNotification;

    private Boolean isAdvertisement;

    private Boolean isPrivacy;

    public GetMemberRes toDto(Member member) {
        GetMemberRes getMemberRes = new GetMemberRes();
        getMemberRes.memberId = member.getMemberId();
        getMemberRes.name = member.getName();
        getMemberRes.type = member.getType();
        getMemberRes.phone = member.getPhone();
        getMemberRes.grade = member.getGrade();
        getMemberRes.needNotification = member.getNeedNotification();
        getMemberRes.isAdvertisement = member.getIsAdvertisement();
        getMemberRes.isPrivacy = member.getIsPrivacy();

        return getMemberRes;
    }

}
