package com.selfrunner.gwalit.global.batch.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class BatchLessonDto {

    private final String name; // Lecture의 name과 binding

    private final LocalDate date; // Lesson의 date와 binding

    private final LocalTime startTime; // Lesson의 startTime과 binding

    private final LocalTime endTime; // Lesson의 endTime과 binding
}
