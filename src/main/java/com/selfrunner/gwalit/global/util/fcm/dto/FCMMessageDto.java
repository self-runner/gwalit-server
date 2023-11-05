package com.selfrunner.gwalit.global.util.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        private String lectureId;
        private String lessonId;
        private String date;
        private String url;
    }

    /**
     * 수업 리포트 등록/수정 시 알람 객체 만드는 메소드
     * @param token - FCM 토큰 정보
     * @param lectureId - 클래스 ID
     * @param lessonId - 수업 리포트 ID
     * @return - 만들어진 FCMMessageDto 반환
     */
    public static FCMMessageDto toDto(String token, String title, String body, String name, Long lectureId, Long lessonId, LocalDate date, String url) {
        return FCMMessageDto.builder()
                .token(token)
                .notification(FCMMessageDto.Notification.builder()
                        .title(title)
                        .body(body)
                        .build())
                .data(Data.builder()
                        .name(name)
                        .lectureId((lectureId != null) ? lectureId.toString() : null)
                        .lessonId((lessonId != null) ? lessonId.toString() : null)
                        .date((date != null) ? date.format(DateTimeFormatter.ofPattern("%y-%M-%d")) : null)
                        .url(url)
                        .build())
                .build();
    }

    public static FCMMessageDto toDto(com.selfrunner.gwalit.domain.notification.entity.Notification notification) {
        System.out.println("T");
        return FCMMessageDto.builder()
                .notification(FCMMessageDto.Notification.builder()
                        .title(notification.getTitle())
                        .body(notification.getBody())
                        .build())
                .data(Data.builder()
                        .name(notification.getName())
                        .lectureId((notification.getLectureId() != null) ? notification.getLectureId().toString() : null)
                        .lessonId((notification.getLessonId() != null) ? notification.getLessonId().toString() : null)
                        .date((notification.getDate() != null) ? notification.getDate().format(DateTimeFormatter.ofPattern("%y-%M-%d")) : null)
                        .url(notification.getUrl())
                        .build())
                .build();
    }
}