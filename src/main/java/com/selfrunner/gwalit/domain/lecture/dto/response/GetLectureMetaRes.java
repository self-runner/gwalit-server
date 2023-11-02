package com.selfrunner.gwalit.domain.lecture.dto.response;

import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lecture.entity.Subject;
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

    private final Subject subject;

    private final String subjectDetail;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final List<Schedule> schedules;

    private final List<MemberMeta> memberMetas;

    public GetLectureMetaRes(Lecture lecture, List<MemberMeta> memberMetas) {
        this.lectureId = lecture.getLectureId();
        this.name = lecture.getName();
        this.color = lecture.getColor();
        this.subject = lecture.getSubject();
        this.subjectDetail = lecture.getSubjectDetail();
        this.startDate = lecture.getStartDate();
        this.endDate = lecture.getEndDate();
        this.schedules = lecture.getSchedules();
        this.memberMetas = memberMetas;
    }
}
