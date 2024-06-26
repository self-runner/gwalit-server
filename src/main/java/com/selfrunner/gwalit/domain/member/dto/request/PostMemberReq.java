package com.selfrunner.gwalit.domain.member.dto.request;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PostMemberReq {

    @NotEmpty(message = "이름이 공란입니다.")
    private String name;

    @NotEmpty(message = "사용자 유형이 공란입니다.")
    private String type;

    @NotNull(message = "전화번호가 Null입니다.")
    @Size(min = 9, max = 12, message = "유효한 전화번호 길이가 아닙니다.")
    @Pattern(regexp = "[0-9]+", message = "숫자만 입력되어야 합니다.")
    private String phone;

    @NotNull(message = "비밀번호가 Null입니다.")
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~@#$%^&*()_\\-\\+=\\[\\]|\\\\;:\\'\",<>\\.?/!])[a-zA-Z\\d~@#$%^&*()_\\-\\+=\\[\\]|\\\\;:\\'\",<>\\.?/!]*$")
    private String password;

    @NotNull(message = "비밀번호가 Null입니다.")
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~@#$%^&*()_\\-\\+=\\[\\]|\\\\;:\\'\",<>\\.?/!])[a-zA-Z\\d~@#$%^&*()_\\-\\+=\\[\\]|\\\\;:\\'\",<>\\.?/!]*$")
    private String passwordCheck;

    @Size(max = 16, message = "학교명은 16자 이내로 입력되어야 합니다.")
    private String school;

    private String grade;

    @NotNull(message = "광고정보 수신 동의가 설정되지 않았습니다.")
    private Boolean isAdvertisement;

    @NotNull(message = "개인정보 처리 방침 동의 여부가 설정되지 않았습니다.")
    private Boolean isPrivacy;

    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .type(this.type)
                .state(MemberState.ACTIVE)
                .phone(this.phone)
                .password(this.password)
                .school(this.school)
                .grade(this.grade)
                .isAdvertisement(this.isAdvertisement)
                .isPrivacy(this.isPrivacy)
                .build();
    }
}
