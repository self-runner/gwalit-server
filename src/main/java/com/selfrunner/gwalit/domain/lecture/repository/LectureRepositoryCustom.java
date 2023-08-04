package com.selfrunner.gwalit.domain.lecture.repository;

import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMetaRes;
import com.selfrunner.gwalit.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface LectureRepositoryCustom {

    Optional<List<Long>> findAllLectureIdByMember(Member member);
    Optional<List<GetLectureMetaRes>> findAllLectureMetaByLectureIdList(List<Long> lectureIdList);
}
