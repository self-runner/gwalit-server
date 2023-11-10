package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PutMemberReq {

    @NotNull
    private Long memberId;

    @NotBlank
    private String name;

    @Size(max = 16, message = "학교명은 16자 이내로 입력되어야 합니다.")
    private String school;

    private String grade;

    @NotNull
    private Boolean isAdvertisement;

    @NotNull
    private Boolean isPrivacy;
}
