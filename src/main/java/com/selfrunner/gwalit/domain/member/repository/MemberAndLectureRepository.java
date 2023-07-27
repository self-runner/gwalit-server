package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAndLectureRepository extends JpaRepository<MemberAndLecture, Long>, MemberAndLectureRepositoryCustom {
}
