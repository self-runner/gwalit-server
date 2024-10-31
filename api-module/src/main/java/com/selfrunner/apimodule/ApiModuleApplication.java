package com.selfrunner.apimodule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableAsync
//@EnableJpaAuditing
//@EnableScheduling
//@EnableBatchProcessing
@Slf4j
@SpringBootApplication(scanBasePackages = "com.selfrunner")
public class ApiModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiModuleApplication.class, args);
        log.info("Gwarit Server Started!");
    }

}
