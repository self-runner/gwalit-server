package com.selfrunner.gwalit.domain.lecture.entity;

import lombok.Getter;

@Getter
public enum Subject {
    // 국어 (중1국어, 중2국어, 중3국어, 독서, 문학, 화법과 작문, 언어와 매체)
    M1KOR, M2KOR, M3KOR, READ, LITERATURE, GRAMMER, LANGUAGE,

    // 수학 (중1수학, 중2수학, 중3수학, 수학1, 수학2, 미적분, 확률과 통계)
    M1MATH, M2MATH, M3MATM, MATH1, MATH2, DIC, PAS,

    // 영어 (중1영어, 중2영어, 중3영어, 영어)
    M1ENG, M2ENG, M3ENG, ENG,

    // 사회 (사회1, 사회2, 역사1, 역사2, 생활과 윤리, 사회문화, 윤리와 사상, 정치와 법, 경제, 동아시아사, 세계사, 한국지리, 세계지리)
    SOC1, SOC2, HIS1, HIS2, EVERYDAYETHICS, SOCIALSTUDIESANDCULTURE, ETHICALIDEOLOGY, POLIRICSANDLAW, ECONOMICS, EASEASISHISTORY, WORLDHISTORY, KOREANGEOGRAPHY, INTERNATIONALGEOGRAPHY,

    // 과학 (중1과학, 중2과학, 중3과학, 통합과학, 물리, 화학, 생명과학, 지구과학)
    M1SCI, M2SCI, M3SCI, COMMONSCIENCE, PHYSICS, CHEMISTRY, BIOSCIENCE, GEOSCIENCE,

    // 기타
    ETC,


    // 참고 레퍼런스: https://staraube.tistory.com/8
}
