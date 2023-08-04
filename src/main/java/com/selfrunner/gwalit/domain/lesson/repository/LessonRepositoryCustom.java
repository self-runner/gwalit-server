package com.selfrunner.gwalit.domain.lesson.repository;

import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;

import java.util.List;
import java.util.Optional;

public interface LessonRepositoryCustom {
    Optional<List<LessonMetaRes>> findAllLessonMetaByLectureId(Long lectureId);
}
