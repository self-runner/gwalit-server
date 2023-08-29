package com.selfrunner.gwalit.domain.lesson.repository;

import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonProgressRes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LessonRepositoryCustom {
    Optional<List<LessonMetaRes>> findAllLessonMetaByLectureId(Long lectureId);

    Optional<List<LessonMetaRes>> findAllLessonMetaByYearMonth(List<Long> lectureIdList, String year, String month);

    Optional<List<LessonProgressRes>> findAllProgressByLectureId(Long lectureId);

    List<Long> findAllLessonIdByLectureId(Long lectureId);

    Optional<LessonMetaRes> findLessonMetaByLectureIdBeforeNow(Long lectureId);

    Optional<LessonMetaRes> findLessonMetaByLectureIdAfterNow(Long lectureId);

    Optional<List<LessonMetaRes>> findAllLessonMetaByLectureIdAndDate(Long lectureId);

    void deleteAllByLectureIdAndDate(Long lectureId, LocalDate startDate, LocalDate endDate);
}
