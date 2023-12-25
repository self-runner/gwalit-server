//package com.selfrunner.gwalit.global.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//
//@Configuration
//public class GcsConfig {
//
//    @Value("${gcp.json}")
//    private String serializedJson;
//
//    @Value("${gcp.project-id}")
//    private String projectId;
//
//    @Bean
//    public Storage storage() throws IOException {
////        // 문자열을 바이트 배열로 변환
////        byte[] byteArray = serializedJson.getBytes(StandardCharsets.UTF_8);
////
////        // ByteArrayInputStream을 사용하여 바이트 배열을 InputStream으로 변환
////        InputStream inputStream = new ByteArrayInputStream(byteArray);
//
//        ClassPathResource resource = new ClassPathResource("starlit-fire-408511-434704594f35.json");
//
//        int data;
//
//        // InputStream에서 데이터를 읽어오고, 더 이상 읽을 데이터가 없을 때까지 반복
//        while ((data = resource.getInputStream().read()) != -1) {
//            // 데이터를 화면에 출력
//            System.out.print((char) data);
//        }
//
//        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
//        return StorageOptions.newBuilder()
//                .setProjectId(projectId)
//                .setCredentials(credentials)
//                .build()
//                .getService();
//    }
//}
