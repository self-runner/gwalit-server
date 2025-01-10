package com.selfrunner.batchmodule.config;

import com.selfrunner.batchmodule.NotificationTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@EnableScheduling
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final NotificationTasklet notificationTasklet;

    @Bean
    public Job notificationJob() {
        return jobBuilderFactory.get("Lesson Notification Job")
                .start(sendLessonInformNotification())
                .build();
    }

    @Bean
    @JobScope
    public Step sendLessonInformNotification() {
        return stepBuilderFactory.get("step")
                .tasklet(notificationTasklet)
                .build();
    }
}
