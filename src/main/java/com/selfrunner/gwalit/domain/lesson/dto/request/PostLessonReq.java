package com.selfrunner.gwalit.domain.lesson.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.domain.lesson.entity.Progress;
import com.selfrunner.gwalit.domain.lesson.entity.Student;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PostLessonReq {

    @NotEmpty(message = "연결된 Class가 없습니다.")
    private String lectureId;

    @NotNull(message = "수업 유형이 선택되지 않았습니다.")
    @Pattern(regexp = "^(Regular|Makeup|Deleted)$\n", message = "올바르지 않은 수업 유형입니다.")
    private String type;

    @NotNull(message = "선택된 학생이 없습니다.")
    private List<Student> students;

    private String feedback;

    private List<Progress> progresses;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private Schedule time;

    public Lesson toEntity(Lecture lecture) {
        Lesson lesson = Lesson.builder()
                .lecture(lecture)
                .type(this.type)
                .students(this.students)
                .feedback(this.feedback)
                .progresses(this.progresses)
                .date(this.date)
                .time(this.time)
                .build();

        return lesson;
    }
}
