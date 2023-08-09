package com.selfrunner.gwalit.domain.lecture.dto.response;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lecture.entity.Rule;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetLectureRes {

    private final Long lectureId;

    private final String name;

    private final String color;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final List<Rule> rules;

    private final List<Schedule> schedules;

    private final List<MemberMeta> memberMetas;

    private final LessonMetaRes lessonMetaRes;
}
