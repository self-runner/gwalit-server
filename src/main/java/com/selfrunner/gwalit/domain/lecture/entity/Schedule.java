package com.selfrunner.gwalit.domain.lecture.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class Schedule {

    private String day;

    private Timestamp startTime;

    private Timestamp endTime;
}
