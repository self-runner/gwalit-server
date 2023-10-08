package com.selfrunner.gwalit.domain.workbook.dto.response;

import com.selfrunner.gwalit.domain.workbook.entity.Subject;
import lombok.RequiredArgsConstructor;

public class GetSubjectRes {

    private String koreanName;

    private String englishName;

    private Boolean isUse;

    public GetSubjectRes(Subject subject, Boolean isUse) {
        this.koreanName = subject.getKoreanName();
        this.englishName = subject.getEnglishName();
        this.isUse = isUse;
    }
}
