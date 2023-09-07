package com.selfrunner.gwalit.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;
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
}
