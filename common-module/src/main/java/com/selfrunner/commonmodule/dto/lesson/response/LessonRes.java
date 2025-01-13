package com.selfrunner.commonmodule.dto.lesson.response;

import com.selfrunner.commonmodule.common.Day;
import com.selfrunner.commonmodule.dto.homework.response.HomeworkRes;
import com.selfrunner.commonmodule.enumerate.lesson.LessonType;
import com.selfrunner.commonmodule.vo.lecture.ScheduleVo;
import com.selfrunner.commonmodule.vo.lesson.Participant;
import com.selfrunner.commonmodule.vo.lesson.Progress;
import com.selfrunner.commonmodule.vo.member.MemberMeta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class LessonRes {

    private Long lessonId;

    private Long lectureId;

    private String color;

    private LessonType type;

    private List<Participant> participants;

    private String feedback;

    private List<Progress> progresses;

    private List<HomeworkRes> homeworks;

    private String date;

    private ScheduleVo time;

    private List<MemberMeta> memberMetas;

    private Boolean isFirst;

    public static LessonRes toDto(Long lessonId,
                                  Long lectureId,
                                  String color,
                                  LessonType type,
                                  List<Participant> participants,
                                  String feedback,
                                  List<Progress> progresses,
                                  List<HomeworkRes> homeworks,
                                  LocalDate date,
                                  Day day,
                                  LocalTime startTime,
                                  LocalTime endTime,
                                  List<MemberMeta> memberMetas,
                                  Boolean isFirst) {
        LessonRes lessonRes = new LessonRes();
        lessonRes.lessonId = lessonId;
        lessonRes.lectureId = lectureId;
        lessonRes.color = color;
        lessonRes.type = type;
        lessonRes.participants = participants;
        lessonRes.feedback = feedback;
        lessonRes.progresses = progresses;
        lessonRes.homeworks = homeworks;
        lessonRes.date = date.toString();
        lessonRes.time = new ScheduleVo(day, startTime.toString(), endTime.toString());
        lessonRes.memberMetas = memberMetas;
        lessonRes.isFirst = isFirst;

        return lessonRes;
    }

}
