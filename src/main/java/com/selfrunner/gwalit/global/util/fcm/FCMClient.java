package com.selfrunner.gwalit.global.util.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMClient {

    public String sendNotification(String content, String token) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .putData("title", content)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            // return response if firebase messaging is successfully completed.
            return response;

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public void sendMessageTo(String targetToken, String title, String body, String id, String isEnd) throws IOException {
        String message = makeMessage(targetToken, title, body, id, isEnd);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(FIREBASE_ALARM_SEND_API_URI)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        log.info(response.body().string());
    }
}
