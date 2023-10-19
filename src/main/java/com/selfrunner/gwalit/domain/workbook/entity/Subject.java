package com.selfrunner.gwalit.domain.workbook.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Subject {
    // 국어
    KOREAN("국어", "korean"),
    // 영어
    ENGLISH("영어", "english"),
    // 수학
    MATH("수학", "math"),
    // 사회
    SOCIETY("사회", "society"),
    // 과학
    SCIENCE("과학", "science"),
    // 기타
    ETC("기타", "etc");

    private final String koreanName;
    private final String englishName;

    Subject(String koreanName, String englishName) {
        this.koreanName = koreanName;
        this.englishName = englishName;
    }
}
