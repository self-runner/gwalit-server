package com.selfrunner.gwalit.domain.lesson.repository;

import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonProgressRes;

import java.util.List;
import java.util.Optional;

public interface LessonRepositoryCustom {
    Optional<List<LessonMetaRes>> findAllLessonMetaByLectureId(Long lectureId);

    Optional<List<LessonMetaRes>> findAllLessonMetaByYearMonth(List<Long> lectureIdList, String year, String month);

    Optional<List<LessonProgressRes>> findAllProgressByLectureId(Long lectureId);
}
