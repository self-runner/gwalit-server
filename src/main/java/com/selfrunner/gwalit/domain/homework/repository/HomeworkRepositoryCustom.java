package com.selfrunner.gwalit.domain.homework.repository;

import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkMainRes;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkStatisticsRes;
import com.selfrunner.gwalit.domain.homework.dto.service.HomeworkRemind;
import com.selfrunner.gwalit.domain.member.entity.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HomeworkRepositoryCustom {

    void deleteHomeworkByLessonId(Long lessonId);

    List<HomeworkRes> findAllByMemberIdAndLessonId(Long memberId, Long lessonId);

    void deleteAllByLessonIdList(List<Long> lessonIdList);

    Optional<List<HomeworkMainRes>> findRecentHomeworkByMemberAndLessonIdList(Member member, List<Long> lessonIdList);

    Optional<List<HomeworkMainRes>> findAllHomeworkByMember(Member member);

    Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndType(Member member, Boolean type);

    HomeworkMainRes findHomeworkByHomeworkId(Long homeworkId);

    Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndLectureId(Member member, Long lectureId);

    Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndLectureIdAndType(Member member, Long lectureId, Boolean type);

    void deleteAllByLessonIdAndMemberIdList(Long lessonId, List<Long> deleteIdList);

    List<HomeworkStatisticsRes> findAllByBodyAndCreatedAt(Long memberId, Long lessonId, String body, LocalDate deadline, LocalDateTime createdAt);

    List<HomeworkRemind> findHomeworkByIsFinish(List<Long> homeworkIdList);
}
