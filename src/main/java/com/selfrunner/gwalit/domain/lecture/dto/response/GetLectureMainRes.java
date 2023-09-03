package com.selfrunner.gwalit.domain.lecture.dto.response;

import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetLectureMainRes {

    private final Long lectureId;

    private final String name;

    private final String color;

    private final List<MemberMeta> memberMetas;
}
