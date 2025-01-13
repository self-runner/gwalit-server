package com.selfrunner.commonmodule.vo.lecture;

import com.selfrunner.commonmodule.common.Day;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class ScheduleVo {

    private Day weekday;

    private String startTime;

    private String endTime;

    public ScheduleVo(Day weekday, LocalTime startTime, LocalTime endTime) {
        this.weekday = weekday;
        this.startTime = startTime.toString();
        this.endTime = endTime.toString();
    }

    public ScheduleVo(Day weekday, String startTime, String endTime) {
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ScheduleVo(String weekday, LocalTime startTime, LocalTime endTime) {
        this.weekday = Day.valueOf(weekday);
        this.startTime = startTime.toString();
        this.endTime = endTime.toString();
    }
}
