package com.selfrunner.gwalit.domain.workbook.dto.response;

import com.selfrunner.gwalit.domain.workbook.entity.*;
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
    public WorkbookRes(Workbook workbook, DifficultyRes difficulty) {
        this.workbookId = workbook.getWorkbookId();
        this.title = workbook.getTitle();
        this.type = workbook.getType().getKoreanName();
        this.subject = workbook.getSubject().getKoreanName();
        this.subjectDetail = workbook.getSubjectDetail().getKoreanName();
        this.chapter = workbook.getChapter();
        this.thumbnailUrl = workbook.getThumbnailUrl();
        this.workbookFileUrl = workbook.getWorkbookFileUrl();
        this.answerFileUrl = workbook.getAnswerFileUrl();
        this.problemCount = workbook.getProblemCount();
        this.time = workbook.getTime();
        this.explanation = workbook.getExplanation();
        this.provider = workbook.getProvider();
        this.copyright = workbook.getCopyright();
        this.isFile = workbook.getIsFile();
        this.difficulty = difficulty;
        this.viewCount = workbook.getViews().getCount();
        this.modifiedAt = workbook.getModifiedAt();
    }
}
