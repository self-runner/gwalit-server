package com.selfrunner.apimodule.presentation.lesson;

import com.selfrunner.apimodule.global.auth.Auth;
import com.selfrunner.commonmodule.common.ApplicationResponse;
import com.selfrunner.commonmodule.dto.lesson.request.PatchLessonMetaRes;
import com.selfrunner.commonmodule.dto.lesson.request.PostLessonReq;
import com.selfrunner.commonmodule.dto.lesson.request.PutLessonIdReq;
import com.selfrunner.commonmodule.dto.lesson.request.PutLessonReq;
import com.selfrunner.commonmodule.dto.lesson.response.LessonIdRes;
import com.selfrunner.commonmodule.dto.lesson.response.LessonMetaRes;
import com.selfrunner.commonmodule.dto.lesson.response.LessonProgressRes;
import com.selfrunner.commonmodule.dto.lesson.response.LessonRes;
import com.selfrunner.apimodule.application.lesson.LessonService;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lesson")
@Tag(name = "Lesson", description = "수업 별 기본 정보 관련")
public class LessonController {

    private final LessonService lessonService;

    @Operation(summary = "수업 리포트 생성")
    @PostMapping("")
    public ApplicationResponse<LessonIdRes> register(@Auth Member member, @Valid @RequestBody PostLessonReq postLessonReq) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, lessonService.register(member, postLessonReq));
    }

    @Operation(summary = "수업 리포트 수정")
    @PutMapping("/{lesson_id}")
    public ApplicationResponse<LessonRes> update(@Auth Member member, @PathVariable("lesson_id") Long lessonId, @Valid @RequestBody PutLessonReq putLessonReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lessonService.update(member, lessonId, putLessonReq));
    }

    @Operation(summary = "수업 리포트 일부 정보 수정")
    @PatchMapping("/{lesson_id}")
    public ApplicationResponse<LessonMetaRes> updateMeta(@Auth Member member, @PathVariable("lesson_id") Long lessonId, @Valid @RequestBody PatchLessonMetaRes patchLessonMetaRes) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lessonService.updateMeta(member, lessonId, patchLessonMetaRes));
    }

    @Operation(summary = "기존 수업 리포트들 모두 삭제")
    @PutMapping("/deleted/{lecture_id}")
    public ApplicationResponse<Void> deleteAll(@Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody List<PutLessonIdReq> putLessonIdReqList) {
        lessonService.deleteAll(member, lectureId, putLessonIdReqList);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "수업 리포트 삭제")
    @DeleteMapping("/{lesson_id}")
    public ApplicationResponse<Void> delete(@Auth Member member, @PathVariable("lesson_id") Long lessonId) {
        lessonService.delete(member, lessonId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "수업 리포트 반환")
    @GetMapping("/{lesson_id}")
    public ApplicationResponse<LessonRes> get(@Auth Member member, @PathVariable("lesson_id") Long lessonId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lessonService.get(member, lessonId));
    }

    @Operation(summary = "수업 리포트 전체 반환 (진도 정보 제외)")
    @GetMapping("/list/{lecture_id}")
    public ApplicationResponse<List<LessonMetaRes>> getAllLessonMeta(@Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lessonService.getAllLessonMeta(member, lectureId));
    }

    @Operation(summary = "월별 수업 정보 반환")
    @GetMapping("/calendar/{year}/{month}")
    public ApplicationResponse<List<LessonMetaRes>> getAllLessonMetaByYearMonth(@Auth Member member, @PathVariable("year") String year, @PathVariable("month") String month) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lessonService.getAllLessonMetaByYearMonth(member, year, month));
    }

    @Operation(summary = "진도 리스트 전체 반환")
    @GetMapping("/progress/{lecture_id}")
    public ApplicationResponse<List<LessonProgressRes>> getAllProgress(@Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lessonService.getAllProgress(member, lectureId));
    }
}
