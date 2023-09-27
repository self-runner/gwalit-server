package com.selfrunner.gwalit.domain.workbook.dto.request;

import com.selfrunner.gwalit.domain.workbook.entity.Problem;
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

    private String problemBody;

    @NotNull
    private Integer answer;

    private String solveBody;

    @NotNull
    private String source;

    @Pattern(regexp = "")
    private String difficulty;

    public Problem toEntity(String problemUrl, String solveUrl) {
        return Problem.builder()
                .subject(this.subject)
                .subjectDetail(this.subjectDetail)
                .type(this.type)
                .problemUrl(problemUrl)
                .problemBody(this.problemBody)
                .answer(this.answer)
                .solveUrl(solveUrl)
                .solveBody(this.solveBody)
                .source(this.source)
                .difficulty(this.difficulty)
                .build();
    }
}
