package com.selfrunner.gwalit.domain.homework.repository;

import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkMainRes;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
import com.selfrunner.gwalit.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface HomeworkRepositoryCustom {

    void deleteHomeworkByLessonId(Long lessonId);

    List<HomeworkRes> findAllByMemberIdAndLessonId(Long memberId, Long lessonId);

    void deleteAllByLessonIdList(List<Long> lessonIdList);

    Optional<List<HomeworkMainRes>> findRecentHomeworkByMemberAndLessonIdList(Member member, List<Long> lessonIdList);
}
