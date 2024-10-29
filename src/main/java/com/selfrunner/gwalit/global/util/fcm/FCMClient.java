package com.selfrunner.gwalit.global.util.fcm;

import com.google.firebase.messaging.*;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMClient {

    /**
     * 1:1 단 건 발송
     * @param message - Firebase Message 객체
     */
    @Async
    public void send(Message message) {
        try {
            FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
            throw new ApplicationException(ErrorCode.FAILED_SEND_MESSAGE);
        }
    }

    /**
     * 다수에게 단 건 발송
     * @param messageList Firebase Message 객체 리스트
     */
    public void sendAll(List<Message> messageList) {
        BatchResponse response;
        try {
            // 알림 발송
            response = FirebaseMessaging.getInstance().sendEachAsync(messageList).get();

            // 요청에 대한 응답 처리
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();

                for (SendResponse sendResponse : responses) {
                    if (!sendResponse.isSuccessful()) {
                        log.info("Failed to send message to token: {}", sendResponse.getMessageId() + sendResponse.getException().getMessage());
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("cannot send to memberList push message. error info : {}", e.getMessage());
            throw new ApplicationException(ErrorCode.FAILED_SEND_MESSAGE);
        }
    }

    @Async
    public void sendMulticast(List<String> tokenList, MulticastMessage multicastMessage) {
        BatchResponse response;
        try {
            response = FirebaseMessaging.getInstance().sendEachForMulticastAsync(multicastMessage).get();

            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();

                for (SendResponse sendResponse : responses) {
                    if (!sendResponse.isSuccessful()) {
                        log.info("Failed to send message to token: {}", sendResponse.getMessageId() + sendResponse.getException().getMessage());
                    }
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            log.error("cannot send to memberList push message. error info: {}", e.getMessage());
            throw new ApplicationException(ErrorCode.FAILED_SEND_MESSAGE);
        }
    }

    public Message makeMessage(String token, String title, String body, String name, Long lectureId, Long lessonId, LocalDate date, String url, Long boardId) {
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
                .putData("boardId", (boardId != null) ? boardId.toString() : "")
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
                .putData("boardId", (notification.getBoardId() != null) ? notification.getBoardId().toString() : "")
                .addAllTokens(tokenList)
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())
                .build();
    }

    /**
     * 알림 발송 시, UTF-8 이모지 제거 (현재 미사용)
     * @param content 발송할 알림 내용
     * @return 이모지가 제거된 알림 내용
     */
    private String removeEmojis(String content) {
        String regexOfEmojis = "[\uD83C-\uDBFF\uDC00-\uDFFF]+";
        return content.replaceAll(regexOfEmojis, "");
    }
}
