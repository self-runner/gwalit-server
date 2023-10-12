package com.selfrunner.gwalit.domain.lecture.dto.request;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PostStudentReq {

    @NotEmpty(message = "이름이 공란입니다.")
    private String name;

    @NotNull(message = "전화번호가 Null입니다.")
    @Size(min = 9, max = 12, message = "유효한 전화번호 길이가 아닙니다.")
    @Pattern(regexp = "[0-9]+", message = "숫자만 입력되어야 합니다.")
    private String phone;

    public Member toEntity() {
        Member member = Member.builder()
                .name(this.name)
                .type(MemberType.STUDENT.toString())
                .state(MemberState.FAKE)
                .phone(this.phone)
                .password(UUID.randomUUID().toString())
                .isAdvertisement(Boolean.FALSE)
                .isPrivacy(Boolean.FALSE)
                .build();

        return member;
    }
}
