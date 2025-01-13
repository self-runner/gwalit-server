package com.selfrunner.commonmodule.dto.lecture.response;

import com.selfrunner.commonmodule.enumerate.lecture.Subject;
import com.selfrunner.commonmodule.vo.member.MemberMeta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetLectureMainRes {

    private final Long lectureId;

    private final String name;

    private final String color;

    private final Subject subject;

    private final List<MemberMeta> memberMetas;
}
