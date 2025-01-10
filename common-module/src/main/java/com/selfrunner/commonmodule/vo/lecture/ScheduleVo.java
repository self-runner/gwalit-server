package com.selfrunner.commonmodule.vo.lecture;

import com.selfrunner.commonmodule.common.Day;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScheduleVo {

    private Day weekday;

    private String startTime;

    private String endTime;

    public ScheduleVo(Day weekday, String startTime, String endTime) {
        this.weekday = weekday;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
