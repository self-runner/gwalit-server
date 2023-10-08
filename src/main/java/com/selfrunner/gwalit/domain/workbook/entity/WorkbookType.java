package com.selfrunner.gwalit.domain.workbook.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum WorkbookType {
    // 모의고사
    MOCKEXAM("모의고사", "mockexam"),

    // 단원평가
    CHAPTEREXAM("단원 평가", "chapterexam"),

    // OX퀴즈
    SHORTEXAM("1분 퀴즈", "shortexam");

    private final String koreanName;

    private final String englishName;

    WorkbookType(String koreanName, String englishName) {
        this.koreanName = koreanName;
        this.englishName = englishName;
    }
}
ㅌ