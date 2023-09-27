package com.selfrunner.gwalit.domain.workbook.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class PostProblemReq {

    @Pattern(regexp = "")
    private String subject;

    @Pattern(regexp = "")
    private String subjectDetail;

    @Pattern(regexp = "")
    private String type;

    private String body;

    @NotNull
    private Integer answer;

    private String solveBody;

    @NotNull
    private String source;

    @Pattern(regexp = "")
    private String difficulty;
}
