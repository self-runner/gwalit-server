package com.selfrunner.gwalit.domain.homework.controller;

import com.selfrunner.gwalit.domain.homework.dto.request.HomeworkReq;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkMainRes;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
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
@RequestMapping("/homework")
@Tag(name = "Homework", description = "숙제 관련")
public class HomeworkController {

    private final HomeworkService homeworkService;

    @Operation(summary = "숙제 생성")
    @PostMapping("")
    public ApplicationResponse<Void> register(@Auth Member member, @RequestParam(value = "lesson_id", required = false) Long lessonId, @Valid @RequestBody HomeworkReq homeworkReq) {
        homeworkService.register(member, lessonId, homeworkReq);
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }

    @Operation(summary = "숙제 정보 수정")
    @PutMapping("/{homework_id}")
    public ApplicationResponse<Void> update(@Auth Member member, @PathVariable("homework_id") Long homeworkId, @Valid @RequestBody HomeworkReq homeworkReq) {
        homeworkService.update(member, homeworkId, homeworkReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
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

}
