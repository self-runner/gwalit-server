package com.selfrunner.gwalit.domain.lesson.dto.response;

import com.selfrunner.gwalit.domain.lesson.entity.LessonType;
import com.selfrunner.gwalit.domain.lesson.entity.Participant;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class LessonMetaRes implements Comparable<LessonMetaRes> {

    private final Long lessonId;

    private final Long lectureId;

    private final LessonType type;

    private final LocalDate date;

    private final Schedule time;

    private final List<Participant> participants;

    @Override
    public int compareTo(LessonMetaRes l2) {
        if(this.getDate().equals(l2.getDate())) {
            if(this.getTime().getStartTime().equals(l2.getTime().getStartTime())) {
                if(this.getTime().getEndTime().equals(l2.getTime().getEndTime())) {
                        return 0;
                }
                if(LocalTime.parse(this.getTime().getEndTime(), DateTimeFormatter.ofPattern("HH:mm")).isAfter(LocalTime.parse(l2.getTime().getEndTime(), DateTimeFormatter.ofPattern("HH:mm")))) {
                    return 1;
                }
                if(LocalTime.parse(this.getTime().getEndTime(), DateTimeFormatter.ofPattern("HH:mm")).isBefore(LocalTime.parse(l2.getTime().getEndTime(), DateTimeFormatter.ofPattern("HH:mm")))) {
                    return -1;
                }
            }
            if((LocalTime.parse(this.getTime().getStartTime(), DateTimeFormatter.ofPattern("HH:mm")).isAfter(LocalTime.parse(l2.getTime().getStartTime(), DateTimeFormatter.ofPattern("HH:mm"))))) {
                return 1;
            }
            if((LocalTime.parse(this.getTime().getStartTime(), DateTimeFormatter.ofPattern("HH:mm")).isBefore(LocalTime.parse(l2.getTime().getStartTime(), DateTimeFormatter.ofPattern("HH:mm"))))) {
                return -1;
            }
        }
        if(this.getDate().isAfter(l2.getDate())) {
            return 1;
        }

        return -1;
    }
}
