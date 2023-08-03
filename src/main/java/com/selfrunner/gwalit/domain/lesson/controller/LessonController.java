package com.selfrunner.gwalit.domain.lesson.controller;

import com.selfrunner.gwalit.domain.lesson.dto.request.PostLessonReq;
import com.selfrunner.gwalit.domain.lesson.dto.request.PutLessonReq;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonRes;
import com.selfrunner.gwalit.domain.lesson.service.LessonService;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson")
@Tag(name = "Lesson", description = "수업 별 기본 정보 관련")
public class LessonController {

    private final LessonService lessonService;

    @Operation(summary = "수업 리포트 생성")
    @PostMapping("")
    public ApplicationResponse<Void> register(@Auth Member member, @Valid @RequestBody PostLessonReq postLessonReq) {
        lessonService.register(member, postLessonReq);
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }

    @Operation(summary = "수업 리포트 수정")
    @PutMapping("/{lesson_id}")
    public ApplicationResponse<Void> update(@Auth Member member, @PathVariable("lesson_id") Long lessonId, @Valid @RequestBody PutLessonReq putLessonReq) {
        lessonService.update(member, lessonId, putLessonReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "수업 리포트 반환")
    @GetMapping("/{lesson_id}")
    public ApplicationResponse<LessonRes> get(@Auth Member member, @PathVariable("lesson_id") Long lessonId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lessonService.get(member, lessonId));
    }

    @Operation(summary = "수업 리포트 삭제")
    @DeleteMapping("/{lesson_id}")
    public ApplicationResponse<Void> delete(@Auth Member member, @PathVariable("lesson_id") Long lessonId) {
        lessonService.delete(member, lessonId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "수업 리포트 전체 반환 (진도 정보 제외)")
    @GetMapping("/list/{lecture_id}")
    public ApplicationResponse<List<LessonMetaRes>> getAllLessonMeta(@Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lessonService.getAllLessonMeta(member, lectureId));
    }

}
