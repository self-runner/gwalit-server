package com.selfrunner.commonmodule.dto.lesson.response;

import com.selfrunner.commonmodule.vo.lecture.ScheduleVo;
import com.selfrunner.commonmodule.vo.lesson.Progress;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class LessonProgressRes {

    private final Long lessonId;

    private final Long lectureId;

    private final LocalDate date;

    private final ScheduleVo time;

    private final List<Progress> progresses;
}
