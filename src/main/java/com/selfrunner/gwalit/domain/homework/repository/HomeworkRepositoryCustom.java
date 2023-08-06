package com.selfrunner.gwalit.domain.homework.repository;

import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;

import java.util.List;
import java.util.Optional;

public interface HomeworkRepositoryCustom {

    void deleteHomeworkByLessonId(Long lessonId);

    List<HomeworkRes> findAllByMemberIdAndLessonId(Long memberId, Long lessonId);
}
