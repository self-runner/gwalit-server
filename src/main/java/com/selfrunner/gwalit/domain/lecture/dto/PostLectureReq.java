package com.selfrunner.gwalit.domain.lecture.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostLectureReq {

    @NotNull
    private String name;

    private String color;

    private String month;

    private List<String> rules;

    //private List<>
}
