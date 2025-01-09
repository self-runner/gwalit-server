package com.selfrunner.domainmodule.domain.lecture.repository;

import com.selfrunner.commonmodule.dto.lecture.response.GetLectureMainRes;
import com.selfrunner.commonmodule.dto.lecture.response.GetLectureMetaRes;
import com.selfrunner.domainmodule.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface LectureRepositoryCustom {

    Optional<List<Long>> findAllLectureIdByMember(Member member);

    Optional<List<GetLectureMainRes>> findAllLectureMainByLectureIdList(Member member, List<Long> lectureIdList);

    Optional<List<GetLectureMetaRes>> findAllLectureMetaByLectureIdList(Member member, List<Long> lectureIdList);

    void deleteAllByLectureIdList(List<Long> lectureIdList);
}
