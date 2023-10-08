package com.selfrunner.gwalit.domain.workbook.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum SubjectDetail {

    // 수학 (수학1, 수학2, 미적분, 확률과 통계, 기하)
    MATH1("수학1", "math1"),
    MATH2("수학2", "math2"),
    CALCULUS("미적분", "calculus"),
    STATISTICS("확률과 통계", "statistics"),
    GEOMETRY("기하", "geometry"),

    // 사회탐구 (사회문화, 생활과 윤리, 윤리와 사상, 법과 정치)
    SOCIETY_CULTURE("사회문화", "society_culture"),
    EVERYDAY_ETHICS("생활과 윤리", "everyday_ethics"),
    ETHICS_IDEOLOGY("윤리와 사상", "ehtics_ideology"),
    LAW_POLITICS("법과 정치", "law_politics");

    private final String koreanName;

    private final String englishName;

    SubjectDetail(String koreanName, String englishName) {
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

}
