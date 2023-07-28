package com.selfrunner.gwalit.domain.lecture.controller;

import com.selfrunner.gwalit.domain.lecture.dto.PostLectureReq;
import com.selfrunner.gwalit.domain.lecture.service.LectureService;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/lecture")
@Tag(name = "Lecture", description = "Class 기본 정보 관련")
public class LectureController {

    private final LectureService lectureService;

    @Operation(summary = "Class 생성")
    @PostMapping("")
    public ApplicationResponse<Void> register(@Auth Member member, @Valid @RequestBody PostLectureReq postLectureReq) {
        lectureService.register(member, postLectureReq);
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }

    @Operation(summary = "Class 삭제")
    @DeleteMapping("/{lecture_id}")
    public ApplicationResponse<Void> delete(@Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        lectureService.delete(member, lectureId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }
}
