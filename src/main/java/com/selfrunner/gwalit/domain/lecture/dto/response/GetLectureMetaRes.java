package com.selfrunner.gwalit.domain.lecture.dto.response;

import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetLectureMetaRes {

    private Long lectureId;

    private String name;

    private String color;

    private List<MemberMeta> memberMetas;
}
