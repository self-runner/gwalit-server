package com.selfrunner.gwalit.domain.task.repository;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.task.dto.response.TaskRes;

import java.util.List;
import java.util.Optional;

public interface TaskRepositoryCustom {

    Optional<List<TaskRes>> findAllByMemberId(Member member);

    Optional<List<TaskRes>> findTasksByLectureLectureIdOrderByDeadlineDesc(Long lectureId);
}
