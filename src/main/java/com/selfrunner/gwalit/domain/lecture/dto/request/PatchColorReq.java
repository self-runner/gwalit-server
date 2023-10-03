package com.selfrunner.gwalit.domain.lecture.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@RequiredArgsConstructor
public class PatchColorReq {

    @NotEmpty(message = "Class 색상이 미정입니다.")
    private String color;
}
