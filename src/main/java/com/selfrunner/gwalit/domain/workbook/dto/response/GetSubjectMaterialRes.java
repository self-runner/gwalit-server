package com.selfrunner.gwalit.domain.workbook.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.selfrunner.gwalit.domain.workbook.entity.SubjectDetail;

import java.util.List;

public class GetSubjectMaterialRes {

    @JsonProperty
    private String koreanName;

    @JsonProperty
    private String englishName;

    @JsonProperty
    private Boolean isUse;

    @JsonProperty
    private List<WorkbookTypeRes> workbooks;

    public GetSubjectMaterialRes(SubjectDetail subjectDetail, Boolean isUse, List<WorkbookTypeRes> workbookTypeResList) {
        this.koreanName = subjectDetail.getKoreanName();
        this.englishName = subjectDetail.getEnglishName();
        this.isUse = isUse;
        this.workbooks = workbookTypeResList;
    }
}
