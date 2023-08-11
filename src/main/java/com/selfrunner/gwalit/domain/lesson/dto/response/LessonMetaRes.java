package com.selfrunner.gwalit.domain.lesson.dto.response;

import com.selfrunner.gwalit.domain.lesson.entity.LessonType;
import com.selfrunner.gwalit.domain.lesson.entity.Participant;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class LessonMetaRes implements Comparator<LessonMetaRes> {

    private final Long lessonId;

    private final Long lectureId;

    private final LessonType type;

    private final LocalDate date;

    private final Schedule time;

    private final List<Participant> participants;

    @Override
    public int compare(LessonMetaRes l1, LessonMetaRes l2) {
        if(l1.getDate().equals(l2.getDate())) {
            if(l1.getTime().getStartTime().equals(l2.getTime().getStartTime())) {
                if(l1.getTime().getEndTime().equals(l2.getTime().getEndTime())) {
                        return 0;
                }
                if(Time.valueOf(l1.getTime().getEndTime()).after(Time.valueOf(l2.getTime().getEndTime()))) {
                    return 1;
                }
                if(Time.valueOf(l1.getTime().getEndTime()).before(Time.valueOf(l2.getTime().getEndTime()))) {
                    return -1;
                }
            }
            if(Time.valueOf(l1.getTime().getStartTime()).after(Time.valueOf(l2.getTime().getStartTime()))) {
                return 1;
            }
            if(Time.valueOf(l1.getTime().getStartTime()).before(Time.valueOf(l2.getTime().getStartTime()))) {
                return -1;
            }
        }
        if(l1.getDate().isAfter(l2.getDate())) {
            return 1;
        }

        return -1;
    }
}
