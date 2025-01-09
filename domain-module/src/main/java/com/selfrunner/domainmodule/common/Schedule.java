package com.selfrunner.domainmodule.common;

import com.selfrunner.commonmodule.common.Day;
import com.selfrunner.commonmodule.vo.lecture.ScheduleVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class Schedule {

    @Enumerated(EnumType.STRING)
    private Day weekday;

    private String startTime;

    private String endTime;

    public Schedule(Day weekday, LocalTime startTime, LocalTime endTime) {
        this.weekday = weekday;
        this.startTime = startTime.toString();
        this.endTime = endTime.toString();
    }

    public static Schedule convertToSchedule(ScheduleVo scheduleVo) {
        return new Schedule(scheduleVo.getWeekday(),
                LocalTime.parse(scheduleVo.getStartTime()),
                LocalTime.parse(scheduleVo.getEndTime()));

    }

    public static ScheduleVo convertToScheduleVo(Schedule schedule) {
        return new ScheduleVo(schedule.getWeekday(),
                schedule.getStartTime(),
                schedule.getEndTime());
    }

    public boolean equals(ScheduleVo scheduleVo) {
        return this.weekday.equals(scheduleVo.getWeekday()) &&
                this.startTime.equals(scheduleVo.getStartTime()) &&
                this.endTime.equals(scheduleVo.getEndTime());
    }
}
