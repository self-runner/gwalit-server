package com.selfrunner.domainmodule.domain.task.repository;

import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.commonmodule.dto.task.response.TaskRes;

import java.util.List;
import java.util.Optional;

public interface TaskRepositoryCustom {

    Optional<List<TaskRes>> findAllByMemberId(Member member);

    Optional<List<TaskRes>> findTasksByLectureLectureIdOrderByDeadlineDesc(Long lectureId);

    void deleteAllByLectureIdList(List<Long> lectureIdList);
}
