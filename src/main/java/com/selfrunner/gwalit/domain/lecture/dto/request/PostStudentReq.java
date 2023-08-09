package com.selfrunner.gwalit.domain.lecture.dto.request;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PostStudentReq {

    @NotEmpty(message = "이름이 공란입니다.")
    private String name;

    @NotNull(message = "전화번호가 Null입니다.")
    @Size(min = 10, max = 11, message = "유효한 전화번호 길이가 아닙니다.")
    private String phone;

    public Member toEntity() {
        Member member = Member.builder()
                .name(this.name)
                .type(MemberType.STUDENT.toString())
                .state(MemberState.FAKE)
                .phone(this.phone)
                .isAdvertisement(Boolean.FALSE)
                .isPrivacy(Boolean.FALSE)
                .build();

        return member;
    }
}
