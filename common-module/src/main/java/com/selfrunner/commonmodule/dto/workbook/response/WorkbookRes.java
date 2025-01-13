package com.selfrunner.commonmodule.dto.workbook.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class WorkbookRes {

    private Long workbookId;

    private String title;

    private String type;

    private String subject;

    private String subjectDetail;

    private String chapter;

    private String thumbnailUrl;

    private String workbookFileUrl;

    private String answerFileUrl;

    private Integer problemCount;

    private Integer time;

    private String explanation;

    private String provider;

    private Boolean copyright;

    private Boolean isFile;

    private DifficultyRes difficulty;

    private Integer viewCount;

    private LocalDateTime modifiedAt;

    // 문제 테이블을 별도로 만들어서, 난이도를 관리하게 될 경우, Difficulty를 따로 계산해서 바인딩할 예정
    public WorkbookRes(Long workbookId,
                       String title,
                       String type,
                       String subject,
                       String subjectDetail,
                       String chapter,
                       String thumbnailUrl,
                       String workbookFileUrl,
                       String answerFileUrl,
                       Integer problemCount,
                       Integer time,
                       String explanation,
                       String provider,
                       Boolean copyright,
                       Boolean isFile,
                       Integer viewCount,
                       LocalDateTime modifiedAt,
                       DifficultyRes difficulty) {
        this.workbookId = workbookId;
        this.title = title;
        this.type = type;
        this.subject = subject;
        this.subjectDetail = subjectDetail;
        this.chapter = chapter;
        this.thumbnailUrl = thumbnailUrl;
        this.workbookFileUrl = workbookFileUrl;
        this.answerFileUrl = answerFileUrl;
        this.problemCount = problemCount;
        this.time = time;
        this.explanation = explanation;
        this.provider = provider;
        this.copyright = copyright;
        this.isFile = isFile;
        this.difficulty = difficulty;
        this.viewCount = viewCount;
        this.modifiedAt = modifiedAt;
    }
}
