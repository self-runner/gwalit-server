package com.selfrunner.gwalit.domain.lecture.dto.request;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PostInviteReq {

    @NotNull(message = "전화번호가 Null입니다.")
    private String phone;

    public Member toEntity() {
        Member member = Member.builder()
                .name(this.phone)
                .type(MemberType.STUDENT.toString())
                .state(MemberState.INVITE)
                .phone(this.phone)
                .password(UUID.randomUUID().toString())
                .isAdvertisement(Boolean.FALSE)
                .isPrivacy(Boolean.FALSE)
                .build();

        return member;
    }
}
