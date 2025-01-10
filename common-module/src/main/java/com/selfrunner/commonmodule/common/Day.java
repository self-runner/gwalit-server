package com.selfrunner.commonmodule.common;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Day {
    MON("월요일"),
    TUE("화요일"),
    WED("수요일"),
    THU("목요일"),
    FRI("금요일"),
    SAT("토요일"),
    SUN("일요일");

    private final String DayName;

    Day(String dayName) {
        this.DayName = dayName;
    }
}
