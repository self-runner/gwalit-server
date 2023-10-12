package com.selfrunner.gwalit.domain.workbook.dto.response;

import com.selfrunner.gwalit.domain.workbook.entity.WorkbookType;

public class WorkbookCardRes {

    private Long workbookId;

    private String title;

    // 일관성을 위해, WorkbookType에서 koreanName을 반영하기
    private String type;

    private String thumbnailUrl;

    private Integer problemCount;

    private Integer time;

    private String source;

    private Integer viewCount;

    public WorkbookCardRes(Long workbookId, String title, WorkbookType workbookType, String thumbnailUrl, Integer problemCount, Integer time, String source, Integer viewCount) {
        this.workbookId = workbookId;
        this.title = title;
        this.type = workbookType.getKoreanName();
        this.thumbnailUrl = thumbnailUrl;
        this.problemCount = problemCount;
        this.time = time;
        this.source = source;
        this.viewCount = viewCount;
    }
}
