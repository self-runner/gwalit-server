package com.selfrunner.commonmodule.dto.workbook.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.selfrunner.commonmodule.enumerate.workbook.WorkbookType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WorkbookTypeRes {

    @JsonProperty
    private final String koreanName;

    @JsonProperty
    private final String englishName;

    @JsonProperty
    private final Boolean isUse;

    public WorkbookTypeRes(WorkbookType workbookType, Boolean isUse) {
        this.koreanName = workbookType.getKoreanName();
        this.englishName = workbookType.getEnglishName();
        this.isUse = isUse;
    }
}
