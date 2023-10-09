package com.selfrunner.gwalit.domain.workbook.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.selfrunner.gwalit.domain.workbook.entity.Subject;
import lombok.RequiredArgsConstructor;

public class GetSubjectRes {

    @JsonProperty
    private String koreanName;

    @JsonProperty
    private String englishName;

    @JsonProperty
    private Boolean isUse;

    public GetSubjectRes(Subject subject, Boolean isUse) {
        this.koreanName = subject.getKoreanName();
        this.englishName = subject.getEnglishName();
        this.isUse = isUse;
    }
}
