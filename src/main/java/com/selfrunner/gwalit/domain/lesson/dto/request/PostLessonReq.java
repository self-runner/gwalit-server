package com.selfrunner.gwalit.domain.lesson.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkReq;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.domain.lesson.entity.Participant;
import com.selfrunner.gwalit.domain.lesson.entity.Progress;
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

    @NotNull(message = "연결된 Class가 없습니다.")
    private Long lectureId;

    @NotNull(message = "수업 유형이 선택되지 않았습니다.")
    @Pattern(regexp = "(Regular|Makeup)", message = "올바르지 않은 수업 유형입니다.")
    private String type;

    private List<Participant> participants;

    private String feedback;

    private List<Progress> progresses;

    private List<HomeworkReq> homeworks;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private Schedule time;

    public Lesson toEntity(Lecture lecture) {
        Lesson lesson = Lesson.builder()
                .lecture(lecture)
                .type(this.getType())
                .participants(this.getParticipants())
                .feedback(this.getFeedback())
                .progresses(this.getProgresses())
                .date(this.getDate())
                .time(this.getTime())
                .build();

        return lesson;
    }

    public static Lesson staticToEntity(PostLessonReq postLessonReq, Lecture lecture) {
        Lesson lesson = Lesson.builder()
                .lecture(lecture)
                .type(postLessonReq.getType())
                .participants(postLessonReq.getParticipants())
                .feedback(postLessonReq.getFeedback())
                .progresses(postLessonReq.getProgresses())
                .date(postLessonReq.getDate())
                .time(postLessonReq.getTime())
                .build();

        return lesson;
    }
}
