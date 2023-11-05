package com.selfrunner.gwalit.global.util.fcm.dto;

import com.selfrunner.gwalit.domain.notification.entity.Notification;
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
     * 클래스 초대 시 알람 객체 만드는 메소드
     * @param token - FCM 토큰 정보
     * @param teacherName - 선생님 이름
     * @param lectureName - 클래스 이름
     * @param lectureId- 클래스 ID
     * @return - 만들어진 FCMMessageDto 반환
     */
    public static FCMMessageDto toDto(String token, String teacherName, String lectureName, Long lectureId) {
        Map<String, String> params = new HashMap<>();
        params.put("lectureId", lectureId.toString());

        return FCMMessageDto.builder()
                .token(token)
                .notification(FCMMessageDto.Notification.builder()
                        .title(lectureName + "클래스 초대")
                        .body("[과릿] " + teacherName + " 선생님으로부터 " + lectureName + " 클래스 초대가 도착했습니다." + "\n" + "접속하여 초대된 클래스를 확인해보세요!")
                        .build())
                .data(Data.builder()
                        .name("studentLectureMain")
                        .params(params)
                        .build())
                .build();
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
        params.put("lectureId", notification.getLectureId().toString());
        params.put("lessonId", notification.getLessonId().toString());
        params.put("date", notification.getDate().toString());
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

/*
{
  "message":{
    "topic":"subscriber-updates",
    "notification":{
      "body" : "This week's edition is now available.",
      "title" : "NewsMagazine.com",
    },
    "data" : {
      "volume" : "3.21.15",
      "contents" : "http://www.news-magazine.com/world-week/21659772"
    },
    "android":{
      "priority":"normal"
    },
    "apns":{
      "headers":{
        "apns-priority":"5"
      }
    },
    "webpush": {
      "headers": {
        "Urgency": "high"
      }
    }
  }
}
 */