package com.selfrunner.gwalit.domain.workbook.dto.response;

import com.selfrunner.gwalit.domain.workbook.entity.SubjectDetail;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class GetSubjectMaterialRes {

    private String koreanName;

    private String englishName;

    private Boolean isUse;

    private List<WorkbookTypeRes> workbookTypeResList;

    public GetSubjectMaterialRes(SubjectDetail subjectDetail, Boolean isUse, List<WorkbookTypeRes> workbookTypeResList) {
        this.koreanName = subjectDetail.getKoreanName();
        this.englishName = subjectDetail.getEnglishName();
        this.isUse = isUse;
        this.workbookTypeResList = workbookTypeResList;
    }
}
