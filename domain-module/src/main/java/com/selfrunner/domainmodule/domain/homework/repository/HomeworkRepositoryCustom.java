package com.selfrunner.domainmodule.domain.homework.repository;

import com.selfrunner.commonmodule.dto.homework.response.HomeworkMainRes;
import com.selfrunner.commonmodule.dto.homework.response.HomeworkRes;
import com.selfrunner.commonmodule.dto.homework.response.HomeworkStatisticsRes;
import com.selfrunner.commonmodule.vo.homework.HomeworkRemind;
import com.selfrunner.domainmodule.domain.member.Member;

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

    HomeworkMainRes findHomeworkByHomeworkId(Member member, Long homeworkId);

    Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndLectureId(Member member, Long lectureId);

    Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndLectureIdAndType(Member member, Long lectureId, Boolean type);

    void deleteAllByLessonIdAndMemberIdList(Long lessonId, List<Long> deleteIdList);

    List<HomeworkStatisticsRes> findAllByBodyAndCreatedAt(Long memberId, Long lessonId, String body, LocalDate deadline, LocalDateTime createdAt);

    List<HomeworkRemind> findHomeworkByIsFinish(List<Long> homeworkIdList);
}
