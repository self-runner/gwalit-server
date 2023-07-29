package com.selfrunner.gwalit.domain.lesson.dto.request;

import com.selfrunner.gwalit.domain.lesson.entity.LessonType;
import com.selfrunner.gwalit.domain.lesson.entity.Student;
import com.selfrunner.gwalit.global.common.Schedule;

import java.time.LocalDate;
import java.util.List;

public class PostLessonReq {

    private String lectureId;
    private LessonType type;

    private List<Student> students;

    private String feedback;

    private LocalDate date;

    private Schedule time;
}
