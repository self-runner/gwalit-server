package com.selfrunner.gwalit.domain.task.repository;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.task.entity.Task;

import java.util.List;

public interface TaskRepositoryCustom {

    List<Task> findAllByMemberId(Member member);
}
