package com.selfrunner.gwalit.domain.member.dto.request;

import com.selfrunner.gwalit.domain.member.entity.Member;
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

    private String school;

    private String grade;
}
