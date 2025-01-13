package com.selfrunner.commonmodule.dto.lecture.response;

import com.selfrunner.commonmodule.vo.lecture.ScheduleVo;
import com.selfrunner.commonmodule.enumerate.lecture.Subject;
import com.selfrunner.commonmodule.vo.member.MemberMeta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetLectureMetaRes {

    private final Long lectureId;

    private final String name;

    private final String color;

    private final Subject subject;

    private final String subjectDetail;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final List<ScheduleVo> schedules;

    private final List<MemberMeta> memberMetas;
}
