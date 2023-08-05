package com.selfrunner.gwalit.domain.lesson.dto.response;

import com.selfrunner.gwalit.domain.lesson.entity.Progress;
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

    private final List<Progress> progresses;
}
