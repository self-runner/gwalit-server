package com.selfrunner.commonmodule.vo.batch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BatchNotificationDto {

    private final Long memberId; // memberAndLecture의 memberId와 바인딩

    private final String token; // FCM용 토큰 포함

    private final List<BatchLessonDto> lessonList;

}
