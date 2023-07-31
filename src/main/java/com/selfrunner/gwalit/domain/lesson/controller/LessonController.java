package com.selfrunner.gwalit.domain.lesson.controller;

import com.selfrunner.gwalit.domain.lesson.dto.request.LessonReq;
import com.selfrunner.gwalit.domain.lesson.service.LessonService;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson")
@Tag(name = "Lecture", description = "수업 별 기본 정보 관련")
public class LessonController {

    private final LessonService lessonService;

    @Operation(summary = "수업 리포트 생성")
    @PostMapping("")
    public ApplicationResponse<Void> register(@Auth Member member, LessonReq lessonReq) {
        lessonService.register(member, lessonReq);
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }

    @Operation(summary = "수업 리포트 수정")
    @PutMapping("")
    public ApplicationResponse<Void> update(@Auth Member member) {
        lessonService.update(member);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }
}
