package com.selfrunner.gwalit.domain.workbook.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.selfrunner.gwalit.domain.workbook.entity.WorkbookType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
