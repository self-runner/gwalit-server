package com.selfrunner.gwalit.global.batch;

import com.google.firebase.messaging.Message;
import com.selfrunner.gwalit.domain.lesson.repository.LessonRepository;
import com.selfrunner.gwalit.global.batch.dto.BatchLessonDto;
import com.selfrunner.gwalit.global.batch.dto.BatchNotificationDto;
import com.selfrunner.gwalit.global.util.fcm.FCMClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class NotificationTasklet implements Tasklet {

    private final LessonRepository lessonRepository;
    private final FCMClient fcmClient;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Notification Batch 시작: 수업 여부 알림 일괄 전송");
        log.info(contribution.toString());
        log.info(chunkContext.toString());

        List<Message> messageList = new ArrayList<>();
        Random random = new Random();
        int sleepTime = random.nextInt(9) + 1;
        Thread.sleep(sleepTime * 1000);
        List<Long> lessonIdList = lessonRepository.findTodayLessonIdByDate(LocalDate.now());
        lessonRepository.updateLessonProcessingByDate(lessonIdList);
        List<BatchNotificationDto> batchNotificationDtoList = lessonRepository.findAllByDate(lessonIdList);
        for(BatchNotificationDto notificationDto : batchNotificationDtoList) {
            String title = "오늘은 수업이 총 " + notificationDto.getLessonList().size() + "개 있어요." + "\n";
            StringBuilder body = new StringBuilder();
            for(BatchLessonDto lessonDto:  notificationDto.getLessonList()) {
                String temp = lessonDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " ~ " + lessonDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " " + lessonDto.getName() + "\n";
                body.append(temp);
            }
            // 마지막 개행 문자 제거
            if(body.length() > 0 && body.charAt(body.length() - 1) == '\n') {
                body.setLength(body.length() - 1);
            }
            messageList.add(fcmClient.makeMessage(notificationDto.getToken(), title, body.toString(), "teacherScheduleManagement", null, null, LocalDate.now(), null));
        }
        if(!messageList.isEmpty()) {
            fcmClient.sendAll(messageList);
        }
        lessonRepository.updateLessonSentByDate(lessonIdList);

        return RepeatStatus.FINISHED;
    }
}
