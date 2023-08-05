package com.selfrunner.gwalit.domain.lesson.dto.response;

import com.selfrunner.gwalit.domain.lesson.entity.Lesson;
import com.selfrunner.gwalit.domain.lesson.entity.LessonType;
import com.selfrunner.gwalit.domain.lesson.entity.Participant;
import com.selfrunner.gwalit.domain.lesson.entity.Progress;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.Part;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class LessonRes {

    private Long lessonId;

    private Long lectureId;

    private LessonType type;

    private List<Participant> participants;

    private String feedback;

    private List<Progress> progresses;

    private String date;

    private Schedule time;

    public LessonRes toDto(Lesson lesson) {
        LessonRes lessonRes = new LessonRes();
        lessonRes.lessonId = lesson.getLessonId();
        lessonRes.lectureId = lesson.getLecture().getLectureId();
        lessonRes.type = lesson.getType();
        lessonRes.participants = lesson.getParticipants();
        lessonRes.feedback = lesson.getFeedback();
        lessonRes.progresses = lesson.getProgresses();
        lessonRes.date = lesson.getDate().toString();
        lessonRes.time = lesson.getTime();

        return lessonRes;
    }

}
