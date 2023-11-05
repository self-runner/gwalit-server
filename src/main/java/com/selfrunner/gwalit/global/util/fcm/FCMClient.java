package com.selfrunner.gwalit.global.util.fcm;

import com.google.firebase.messaging.*;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMClient {

    /**
     * 1:1 단 건 발송
     * @param
     * @return
     */
    public String send(Message message) {
        try {
            // Message message = makeMessage(fcmMessageDto);

            String response = FirebaseMessaging.getInstance().send(message);
            // return response if firebase messaging is successfully completed.
            return response;

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            throw new ApplicationException(ErrorCode.FAILED_SEND_MESSAGE);
        }
    }

//    public void sendAll(List<FCMMessageDto> fcmMessageDtoList) {
//        BatchResponse response;
//        try {
//            List<Message> messageList = fcmMessageDtoList.stream()
//                    .map(this::makeMessage)
//                    .collect(Collectors.toList());
//
//            // 알림 발송
//            response = FirebaseMessaging.getInstance().sendAll(messageList);
//
//            // 요청에 대한 응답 처리
//            if (response.getFailureCount() > 0) {
//                List<SendResponse> responses = response.getResponses();
//                List<String> failedTokens = new ArrayList<>();
//
//                for (int i = 0; i < responses.size(); i++) {
//                    if (!responses.get(i).isSuccessful()) {
//                        failedTokens.add(fcmMessageDtoList.get(i).getToken());
//                    }
//                }
//                log.error("List of tokens are not valid FCM token : " + failedTokens);
//            }
//        } catch (FirebaseMessagingException e) {
//            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
//        }
//    }

    public void sendMulticast(List<String> tokenList, MulticastMessage multicastMessage) {
        BatchResponse response;
        try {
            //MulticastMessage multicastMessage = makeMulticastMessage(tokenList, fcmMessageDto);
            response = FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        // The order of responses corresponds to the order of the registration tokens.
                        failedTokens.add(tokenList.get(i));
                    }
                }
                log.error("List of tokens are not valid FCM token : " + failedTokens);
            }
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info: {}", e.getMessage());

        }
    }

    public Message makeMessage(String token, String title, String body, String name, Long lectureId, Long lessonId, LocalDate date, String url) {
        // 딥링크용 정보가 없을 경우
        if(name == null) {
            return Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build()) // Priority High 설정
                    .build();
        }

        // 딥링크용 정보가 있는 경우
        return Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putData("name", name)
                .putData("lectureId", (lectureId != null) ? lectureId.toString() : "")
                .putData("lessonId", (lessonId != null) ? lessonId.toString() : "")
                .putData("date", (date != null) ? date.format(DateTimeFormatter.ofPattern("%y-%M-%d")) : "")
                .putData("url", (url != null) ? url : "")
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build()) // Priority High 설정
                .build();
    }

    public MulticastMessage makeMulticastMessage(List<String> tokenList, com.selfrunner.gwalit.domain.notification.entity.Notification notification) {
        if(notification.getName() == null) {
            return MulticastMessage.builder()
                    .setNotification(Notification.builder()
                            .setTitle(notification.getTitle())
                            .setBody(notification.getBody())
                            .build())
                    .addAllTokens(tokenList)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build())
                    .build();
        }
        return MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(notification.getTitle())
                        .setBody(notification.getBody())
                        .build())
                .putData("name", notification.getName())
                .putData("lectureId", (notification.getLectureId() != null) ? notification.getLectureId().toString() : "")
                .putData("lessonId", (notification.getLessonId() != null) ? notification.getLessonId().toString() : "")
                .putData("date", (notification.getDate() != null) ? notification.getDate().format(DateTimeFormatter.ofPattern("%y-%M-%d")) : "")
                .putData("url", (notification.getUrl() != null) ? notification.getUrl() : "")
                .addAllTokens(tokenList)
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
                .build();
    }
}
