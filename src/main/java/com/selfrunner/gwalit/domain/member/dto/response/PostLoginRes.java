package com.selfrunner.gwalit.domain.member.dto.response;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberGrade;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import com.selfrunner.gwalit.global.util.jwt.TokenDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostLoginRes {

    private String accessToken;

    private String refreshToken;

    private Long memberId;

    private String name;

    private MemberType type;

    private String phone;

    private String school;

    private MemberGrade grade;

    private Boolean needNotification;

    private Boolean isAdvertisement;

    private Boolean isPrivacy;

    public PostLoginRes toDto(TokenDto tokenDto, Member member) {
        PostLoginRes postLoginRes = new PostLoginRes();
        postLoginRes.accessToken = tokenDto.getAccessToken();
        postLoginRes.refreshToken = tokenDto.getRefreshToken();
        postLoginRes.memberId = member.getMemberId();
        postLoginRes.name = member.getName();
        postLoginRes.type = member.getType();
        postLoginRes.phone = member.getPhone();
        postLoginRes.school = member.getSchool();
        postLoginRes.grade = member.getGrade();
        postLoginRes.needNotification = member.getNeedNotification();
        postLoginRes.isAdvertisement = member.getIsAdvertisement();
        postLoginRes.isPrivacy = member.getIsPrivacy();

        return postLoginRes;
    }
}
