package com.selfrunner.gwalit.domain.workbook.dto.response;

import com.selfrunner.gwalit.domain.workbook.entity.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostProblemRes {

    private Long problemId;

    private Subject subject;

    private SubjectDetail subjectDetail;

    private ProblemType type;

    private String problemUrl;

    private String problemBody;

    private Integer answer;

    private String solveUrl;

    private String solveBody;

    private String source;

    private Difficulty difficulty;

    public static PostProblemRes toDto(Problem problem) {
        PostProblemRes postProblemRes = new PostProblemRes();
        postProblemRes.problemId = problem.getProblemId();
        postProblemRes.subject = problem.getSubject();
        postProblemRes.subjectDetail = problem.getSubjectDetail();
        postProblemRes.type = problem.getType();
        postProblemRes.problemUrl = problem.getProblemUrl();
        postProblemRes.problemBody = problem.getProblemBody();
        postProblemRes.answer = problem.getAnswer();
        postProblemRes.solveUrl = problem.getSolveUrl();
        postProblemRes.solveBody = problem.getSolveBody();
        postProblemRes.source = problem.getSource();
        postProblemRes.difficulty = problem.getDifficulty();

        return postProblemRes;
    }
}
