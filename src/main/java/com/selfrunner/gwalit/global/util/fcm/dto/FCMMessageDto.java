package com.selfrunner.gwalit.global.util.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class FCMMessageDto {
    private String token;
    private Notification notification;
    private Data data;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Notification {
        private String  title;
        private String  body;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Data {
        private String name;
        private Map<String, String> params;
    }

    /**
     * 수업 리포트 등록/수정 시 알람 객체 만드는 메소드
     * @param token - FCM 토큰 정보
     * @param lectureId - 클래스 ID
     * @param lessonId - 수업 리포트 ID
     * @return - 만들어진 FCMMessageDto 반환
     */
    public static FCMMessageDto toDto(String token, String title, String body, String name, Long lectureId, Long lessonId, LocalDate date, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("lectureId", lectureId.toString());
        params.put("lessonId", lessonId.toString());
        params.put("date", date.format(DateTimeFormatter.ofPattern("%y-%M-%d")));
        params.put("url", url);

        return FCMMessageDto.builder()
                .token(token)
                .notification(FCMMessageDto.Notification.builder()
                        .title(title)
                        .body(body)
                        .build())
                .data(Data.builder()
                        .name(name)
                        .params(params)
                        .build())
                .build();
    }

    public static FCMMessageDto toDto(com.selfrunner.gwalit.domain.notification.entity.Notification notification) {
        Map<String, String> params = new HashMap<>();
        params.put("lectureId", (notification.getLectureId() != null) ? notification.getLectureId().toString() : null);
        params.put("lessonId", (notification.getLessonId() != null) ? notification.getLessonId().toString() : null);
        params.put("date", (notification.getDate() != null) ? notification.getDate().toString() : null);
        params.put("url", notification.getUrl());

        return FCMMessageDto.builder()
                .notification(FCMMessageDto.Notification.builder()
                        .title(notification.getTitle())
                        .body(notification.getBody())
                        .build())
                .data(Data.builder()
                        .name(notification.getName())
                        .params(params)
                        .build())
                .build();
    }
}