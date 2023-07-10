package com.selfrunner.gwalit.domain.member.dto.request;

import com.selfrunner.gwalit.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PostMemberReq {

    @NotEmpty
    private String name;

    @NotEmpty
    private String type;

    @NotEmpty
    @Size(min = 10, max = 11)
    private String phone;

    @NotNull
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~@#$%^&*()_\\-\\+=\\[\\]|\\\\;:\\'\",<>\\.?/!])[a-zA-Z\\d~@#$%^&*()_\\-\\+=\\[\\]|\\\\;:\\'\",<>\\.?/!]*$")
    private String password;

    private String school;

    private String grade;

    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .type(this.type)
                .phone(this.phone)
                .password(this.password)
                .school(this.school)
                .grade(this.grade)
                .build();
    }
}
