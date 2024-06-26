package com.selfrunner.gwalit.domain.homework.controller;

import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkRemindReq;
import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkReq;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkMainRes;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkStatisticsRes;
import com.selfrunner.gwalit.domain.homework.service.HomeworkService;
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
@RequestMapping("/api/v1/homework")
@Tag(name = "Homework", description = "숙제 관련")
public class HomeworkController {

    private final HomeworkService homeworkService;

    @Operation(summary = "숙제 생성")
    @PostMapping("")
    public ApplicationResponse<HomeworkRes> register(@Auth Member member, @RequestParam(value = "lesson_id", required = false) Long lessonId, @Valid @RequestBody HomeworkReq homeworkReq) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, homeworkService.register(member, lessonId, homeworkReq));
    }

    @Operation(summary = "숙제 정보 수정")
    @PutMapping("/{homework_id}")
    public ApplicationResponse<HomeworkMainRes> update(@Auth Member member, @PathVariable("homework_id") Long homeworkId, @Valid @RequestBody HomeworkReq homeworkReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, homeworkService.update(member, homeworkId, homeworkReq));
    }

    @Operation(summary = "숙제 삭제")
    @DeleteMapping("/{homework_id}")
    public ApplicationResponse<Void> delete(@Auth Member member, @PathVariable("homework_id") Long homeworkId) {
        homeworkService.delete(member, homeworkId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "숙제 정보 반환")
    @GetMapping("/{homework_id}")
    public ApplicationResponse<HomeworkRes> get(@Auth Member member, @PathVariable("homework_id") Long homeworkId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, homeworkService.get(member, homeworkId));
    }

    @Operation(summary = "해당 학생이 가지고 있는 숙제 정보 모두 반환")
    @GetMapping("")
    public ApplicationResponse<List<HomeworkRes>> getAll(@Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, homeworkService.getAll(member));
    }

    @Operation(summary = "메인 페이지용 학생이 가지고 있는 숙제 정보 반환")
    @GetMapping("/main")
    public ApplicationResponse<List<HomeworkMainRes>> getMain(@Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, homeworkService.getMain(member));
    }

    @Operation(summary = "클래스 페이지용 가장 최근 수업에 해당하는 숙제 정보 반환")
    @GetMapping("/lecture/{lecture_id}")
    public ApplicationResponse<List<HomeworkMainRes>> getLecture(@Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, homeworkService.getLecture(member, lectureId));
    }

    @Operation(summary = "학생이 가지고 있는 모든 숙제 리스트 반환")
    @GetMapping("/list")
    public ApplicationResponse<List<HomeworkMainRes>> getList(@Auth Member member, @RequestParam(value = "lectureId", required = false) Long lectureId, @RequestParam("type") String type) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, homeworkService.getList(member, lectureId, type));
    }

    @Operation(summary = "숙제별 학생들 통계 반환")
    @GetMapping("/statistics/{homework_id}")
    public ApplicationResponse<List<HomeworkStatisticsRes>> getStatisticsList(@Auth Member member, @PathVariable("homework_id") Long homeworkId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, homeworkService.getStatisticsList(member, homeworkId));
    }

    @Operation(summary = "학생들 숙제 리마인드 API")
    @PostMapping("/remind")
    public ApplicationResponse<Void> sendHomeworkRemindNotification(@Auth Member member, @Valid @RequestBody List<HomeworkRemindReq> homeworkRemindReqList) {
        homeworkService.sendHomeworkRemindNotification(member, homeworkRemindReqList);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }
}
