package com.selfrunner.gwalit.domain.workbook.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum SubjectDetail {

    // 국어 (화법과 작문, 언어와 매체)
    SPEECH_WRITING("화법과 작문", "speech_writing"),
    LANGUAGE_MEDIA("언어와 매체", "language_media"),

    // 수학 (수학1, 수학2, 미적분, 확률과 통계, 기하)
    MATH1("수학1", "math1"),
    MATH2("수학2", "math2"),
    CALCULUS("미적분", "calculus"),
    STATISTICS("확률과 통계", "statistics"),
    GEOMETRY("기하", "geometry"),

    // 영어 (고등, 중등)
    HIGH("고등", "high"),
    MIDDLE("중등", "middle"),

    // 사회탐구 (사회문화, 생활과 윤리, 윤리와 사상, 법과 정치)
    SOCIETY_CULTURE("사회문화", "society_culture"),
    EVERYDAY_ETHICS("생활과 윤리", "everyday_ethics"),
    ETHICS_IDEOLOGY("윤리와 사상", "ehtics_ideology"),
    LAW_POLITICS("정치와 법", "law_politics"),

    // 과학탐구 (물리, 화학, 생명과학, 지구과학)
    PHYSICS("물리", "physics"),
    CHEMISTRY("화학", "chemistry"),
    BIOSCIENCE("생명과학", "bioscience"),
    GEOSCIENCE("지구과학", "geoscience"),

    // 기타 (한국사)
    KOREAN_HISTORY("한국사", "korean_history");


    private final String koreanName;

    private final String englishName;

    SubjectDetail(String koreanName, String englishName) {
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

}

