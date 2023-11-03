package com.selfrunner.gwalit.global.util.fcm;

import com.google.firebase.messaging.*;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.fcm.dto.FCMMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMClient {

    public String send(FCMMessageDto fcmMessageDto) {
        try {
            Message message = makeMessage(fcmMessageDto);

            String response = FirebaseMessaging.getInstance().send(message);
            // return response if firebase messaging is successfully completed.
            return response;

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            throw new ApplicationException(ErrorCode.FAILED_SEND_MESSAGE);
        }
    }

    public void sendAll(List<FCMMessageDto> fcmMessageDtoList) {
        BatchResponse response;
        try {
            List<Message> messageList = fcmMessageDtoList.stream()
                    .map(this::makeMessage)
                    .collect(Collectors.toList());

            // 알림 발송
            response = FirebaseMessaging.getInstance().sendAll(messageList);

            // 요청에 대한 응답 처리
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();

                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokens.add(fcmMessageDtoList.get(i).getToken());
                    }
                }
                log.error("List of tokens are not valid FCM token : " + failedTokens);
            }
        } catch (FirebaseMessagingException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
        }
    }

    private Message makeMessage(FCMMessageDto fcmMessageDto) {
        // 딥링크용 정보가 없을 경우
        if(fcmMessageDto.getData() == null) {
            return Message.builder()
                    .setToken(fcmMessageDto.getToken())
                    .setNotification(Notification.builder()
                            .setTitle(fcmMessageDto.getNotification().getTitle())
                            .setBody(fcmMessageDto.getNotification().getBody())
                            .build())
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build()) // Priority High 설정
                    .build();
        }

        // 딥링크용 정보가 있는 경우
        return Message.builder()
                .setToken(fcmMessageDto.getToken())
                .setNotification(Notification.builder()
                        .setTitle(fcmMessageDto.getNotification().getTitle())
                        .setBody(fcmMessageDto.getNotification().getBody())
                        .build())
                .putData("name", fcmMessageDto.getData().getName())
                .putData("params", fcmMessageDto.getData().getParams().toString())
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build()) // Priority High 설정
                .build();
    }
}
