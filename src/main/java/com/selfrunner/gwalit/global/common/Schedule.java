package com.selfrunner.gwalit.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class Schedule {

    @Enumerated(EnumType.STRING)
    private Day weekday;

    private String startTime;

    private String endTime;
}
