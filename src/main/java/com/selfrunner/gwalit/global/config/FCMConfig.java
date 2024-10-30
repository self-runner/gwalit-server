package com.selfrunner.gwalit.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Configuration
public class FCMConfig {

    @Value("${firebase.config}")
    private ClassPathResource fcmServiceKey;

    @Value("${firebase.scope}")
    private String scope;

//    @Value("${firebase.config-file}")
//    private String serializedJson;

    @PostConstruct
    public void initialize() {
        try {
//            // 문자열을 바이트 배열로 변환
//            byte[] byteArray = serializedJson.getBytes(StandardCharsets.UTF_8);
//
//            // ByteArrayInputStream을 사용하여 바이트 배열을 InputStream으로 변환
//            InputStream inputStream = new ByteArrayInputStream(byteArray);

            System.out.println(fcmServiceKey);

            // Service Account를 이용하여 Fireabse Admin SDK 초기화
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(fcmServiceKey.getInputStream()).createScoped(List.of(scope)))
                    .build();

            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("FCM Initialize");
            }
        } catch (Exception e) {
            log.error("FCM Initialize Error: {}", e.getMessage());
            throw new ApplicationException(ErrorCode.FAILED_FCM_INIT);
        }
    }

    private String readResourceAsString(ClassPathResource resource) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
