package com.selfrunner.gwalit.domain.lesson.dto.response;

import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.domain.lesson.entity.LessonType;
import com.selfrunner.gwalit.domain.lesson.entity.Progress;
import com.selfrunner.gwalit.domain.lesson.entity.Student;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class LessonRes {

    private String lessonId;

    private String lecturId;

    private LessonType type;

    private List<Student> students;

    private String feedback;

    private List<Progress> progresses;

    private String date;

    private Schedule time;

    public LessonRes toDto(Lesson lesson) {
        LessonRes lessonRes = new LessonRes();
        lessonRes.lessonId = lesson.getLessonId().toString();
        lessonRes.lecturId = lesson.getLecture().getLectureId().toString();
        lessonRes.type = lesson.getType();
        lessonRes.students = lesson.getStudents();
        lessonRes.feedback = lesson.getFeedback();
        lessonRes.progresses = lesson.getProgresses();
        lessonRes.date = lesson.getDate().toString();
        lessonRes.time = lesson.getTime();

        return lessonRes;
    }

}
