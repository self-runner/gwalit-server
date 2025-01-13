package com.selfrunner.domainmodule.domain.member.repository;

import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.domainmodule.domain.member.MemberAndLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAndLectureRepository extends JpaRepository<MemberAndLecture, Long>, MemberAndLectureRepositoryCustom {

    // 사용자 클래스 존재 여부 확인
    Optional<MemberAndLecture> findMemberAndLectureByMemberAndLectureLectureId(Member member, Long lectureId);

    MemberAndLecture findMemberAndLectureByLectureLectureIdAndIsTeacherTrue(Long lectureId);
}
