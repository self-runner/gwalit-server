package com.selfrunner.gwalit.global.batch.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BatchNotificationDto {

    private final Long memberId; // memberAndLecture의 memberId와 바인딩

    private final List<BatchLessonDto> lessonList;

}
