package com.selfrunner.gwalit.domain.lesson.repository;

import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonProgressRes;
import com.selfrunner.gwalit.global.batch.dto.BatchNotificationDto;

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

    void deleteAllByLectureIdAndDate(Long lectureId, LocalDate startDate, LocalDate endDate);

    Optional<Long> findRecentLessonIdByLectureId(Long lectureId);

    Optional<List<Long>> findRecentLessonIdByLectureIdList(List<Long> lectureId);

    List<Long> findAllLessonIdByLectureIdList(List<Long> lectureIdList);

    void deleteAllByLectureLectureIdList(List<Long> lectureIdList);

    List<BatchNotificationDto> findAllByDate(LocalDate date);
}
