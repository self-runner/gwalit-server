package com.selfrunner.gwalit.global.config;

import com.selfrunner.gwalit.global.batch.NotificationTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final NotificationTasklet notificationTasklet;

    @Bean
    public Job notificationJob() {
        Job job = jobBuilderFactory.get("Lesson Notification Job")
                .start(sendLessonInformNotification())
                .build();

        return job;
    }

    @Bean
    @JobScope
    public Step sendLessonInformNotification() {
        return stepBuilderFactory.get("step")
                .tasklet(notificationTasklet)
                .build();
    }
}
