package com.selfrunner.gwalit.domain.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class PutMemberReq {

    @NotNull
    private Long memberId;

    @NotBlank
    private String name;

    private String school;

    private String grade;

    @NotNull
    private Boolean isAdvertisement;

    @NotNull
    private Boolean isPrivacy;
}
