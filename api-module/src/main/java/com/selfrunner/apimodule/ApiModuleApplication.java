package com.selfrunner.apimodule;

import com.selfrunner.batchmodule.GwaritBatchRoot;
import com.selfrunner.commonmodule.GwaritCommonRoot;
import com.selfrunner.domainmodule.GwaritDomainRoot;
import com.selfrunner.inframodule.GwaritInfraRoot;
import com.selfrunner.notificationmodule.GwaritNotificationRoot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@Slf4j
@SpringBootApplication(scanBasePackageClasses = {
        GwaritCommonRoot.class,
        GwaritDomainRoot.class,
        GwaritInfraRoot.class,
        GwaritBatchRoot.class,
        GwaritNotificationRoot.class,
        ApiModuleApplication.class
})
public class ApiModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiModuleApplication.class, args);
        log.info("Gwarit Server Started!");
    }

}
