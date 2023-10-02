package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAndLectureRepository extends JpaRepository<MemberAndLecture, Long>, MemberAndLectureRepositoryCustom {

    // 사용자 클래스 존재 여부 확인
    Optional<MemberAndLecture> findMemberAndLectureByMemberAndLectureLectureId(Member member, Long lectureId);

    // 초대하고자 하는 학생이 이미 클래스에 있는지 확인
    Boolean existsMemberAndLectureByMemberMemberIdAndLectureLectureIdAndDeletedAtIsNull(Long memberId, Long lectureId);
}
