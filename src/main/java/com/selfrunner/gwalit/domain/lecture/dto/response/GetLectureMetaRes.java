package com.selfrunner.gwalit.domain.lecture.dto.response;

import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import com.selfrunner.gwalit.global.common.Schedule;
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

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final List<Schedule> schedules;

    private final List<MemberMeta> memberMetas;
}
