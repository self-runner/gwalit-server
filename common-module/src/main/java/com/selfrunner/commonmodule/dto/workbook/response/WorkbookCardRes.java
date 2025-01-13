package com.selfrunner.commonmodule.dto.workbook.response;

import com.selfrunner.commonmodule.enumerate.workbook.WorkbookType;
import lombok.Getter;

@Getter
public class WorkbookCardRes {

    private Long workbookId;

    private String title;

    // 일관성을 위해, WorkbookType에서 koreanName을 반영하기
    private String type;

    private String thumbnailUrl;

    private Integer problemCount;

    private Integer time;

    private String provider;

    private Integer viewCount;

    private Boolean isNew;

    public WorkbookCardRes(Long workbookId, String title, WorkbookType workbookType, String thumbnailUrl, Integer problemCount, Integer time, String provider, Integer viewCount, Boolean isNew) {
        this.workbookId = workbookId;
        this.title = title;
        this.type = workbookType.getKoreanName();
        this.thumbnailUrl = thumbnailUrl;
        this.problemCount = problemCount;
        this.time = time;
        this.provider = provider;
        this.viewCount = viewCount;
        this.isNew = isNew;
    }
}
