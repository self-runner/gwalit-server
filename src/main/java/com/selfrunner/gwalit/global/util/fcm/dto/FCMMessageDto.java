package com.selfrunner.gwalit.global.util.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FCMMessageDto {
    private boolean    validateOnly;
    private Message    message;


    @Getter
    @Builder
    @AllArgsConstructor
    public static class Message {
        private String token;
        private Notification notification;
        private Data data;
    }

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
        private String description;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class android {
        private String priority;
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