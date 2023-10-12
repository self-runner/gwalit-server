package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.lecture.dto.response.GetStudentRes;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;

import java.util.List;
import java.util.Optional;

public interface MemberAndLectureRepositoryCustom {

    Optional<List<Long>> findLectureIdByMember(Member member);

    Optional<List<MemberMeta>> findMemberMetaByLectureLectureId(Long lectureId);

    Long findCountByMember(Member member);

    void deleteMemberAndLectureByMemberIdList(Long lectureId, List<Long> memberIdList);

    Optional<List<GetStudentRes>> findStudentByMemberAndLectureId(Member member, Long lectureId);

    void deleteMemberAndLectureByLectureId(Long lectureId);

    void deleteMemberAndLecturesByMember(Member member);

    Optional<Lecture> findLectureByMemberIdAndLectureId(Long memberId, Long lectureId);

    Optional<Member> findMemberAndLectureIdByMemberPhoneAndLectureId(String phone, Long lectureId);
}
