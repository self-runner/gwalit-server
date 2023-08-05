package com.selfrunner.gwalit.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class Schedule {

    private String weekday;

    private String startTime;

    private String endTime;
}
