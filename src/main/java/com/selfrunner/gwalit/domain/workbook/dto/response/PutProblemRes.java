package com.selfrunner.gwalit.domain.workbook.dto.response;

import com.selfrunner.gwalit.domain.workbook.entity.*;

public class PutProblemRes {
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

    public static PutProblemRes toDto(Problem problem) {
        PutProblemRes putProblemRes = new PutProblemRes();
        putProblemRes.problemId = problem.getProblemId();
        putProblemRes.subject = problem.getSubject();
        putProblemRes.subjectDetail = problem.getSubjectDetail();
        putProblemRes.type = problem.getType();
        putProblemRes.problemUrl = problem.getProblemUrl();
        putProblemRes.problemBody = problem.getProblemBody();
        putProblemRes.answer = problem.getAnswer();
        putProblemRes.solveUrl = problem.getSolveUrl();
        putProblemRes.solveBody = problem.getSolveBody();
        putProblemRes.source = problem.getSource();
        putProblemRes.difficulty = problem.getDifficulty();

        return putProblemRes;
    }
}
