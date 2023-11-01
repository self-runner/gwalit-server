package com.selfrunner.gwalit.global.batch;

import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.global.batch.dto.BatchNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class NotificationTasklet implements Tasklet {

    private final LessonRepository lessonRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Notification Batch 시작: 수업 여부 알림 일괄 전송");
        log.info(contribution.toString());
        log.info(chunkContext.toString());

        List<BatchNotificationDto> batchNotificationDtoList = lessonRepository.findAllByDate(LocalDate.now());
        for(BatchNotificationDto b : batchNotificationDtoList) {
            System.out.println(b.getMemberId() + ": " + b.getLessonList().size());
        }

        return RepeatStatus.FINISHED;
    }
}
