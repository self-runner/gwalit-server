package com.selfrunner.gwalit.domain.lecture.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@RequiredArgsConstructor
public class PatchNameReq {

    @NotEmpty(message = "Class 이름이 공란입니다.")
    private String name;
}
