package com.selfrunner.gwalit.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
public class FCMConfig {

    @Value("${firebase.config}")
    private String configFile;

    @Value("${firebase.scope}")
    private String scope;

    @PostConstruct
    public void initialize() {
        try {
            // Service Account를 이용하여 Fireabse Admin SDK 초기화
            System.out.println("Test2");
            InputStream serviceAccount = new ClassPathResource(configFile).getInputStream();
            System.out.println("Test3");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            System.out.println("Test4");

            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("FCM Initialize");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("FCM 환경 설정 오류 발생");
            throw new ApplicationException(ErrorCode.FAILED_FCM_INIT);
        }
    }
}
