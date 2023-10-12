package com.selfrunner.gwalit.domain.lecture.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PatchNameReq {

    @NotEmpty(message = "Class 이름이 공란입니다.")
    @Size(min = 2, max = 16, message = "클래스명은 2자 ~ 16자 사이여야 합니다.")
    private String name;
}
