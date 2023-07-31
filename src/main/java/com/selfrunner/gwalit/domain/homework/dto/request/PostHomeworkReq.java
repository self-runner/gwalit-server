package com.selfrunner.gwalit.domain.homework.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.selfrunner.gwalit.domain.homework.entity.Homework;
import com.selfrunner.gwalit.domain.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class PostHomeworkReq {

    private String lessonId;

    @NotEmpty
    private String body;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate deadline;

    @NotNull
    private Boolean isFinish;

    public Homework toEntity(Member member) {
        Homework homework = Homework.builder()
                .lessonId(this.lessonId)
                .memberId(member.getMemberId())
                .body(this.body)
                .deadline(this.deadline)
                .isFinish(this.isFinish)
                .build();

        return homework;
    }
}
