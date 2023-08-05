package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.MemberAndLesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAndLessonRepository extends JpaRepository<MemberAndLesson, Long>, MemberAndLessonRepositoryCustom {
}
