package com.selfrunner.gwalit.domain.lecture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.selfrunner.gwalit.domain.lecture.dto.request.*;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMainRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMetaRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureRes;
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
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


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

    @Operation(summary = "특정 Class 정보 반환")
    @GetMapping("/{lecture_id}")
    public ApplicationResponse<GetLectureMetaRes> get(@Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.get(member, lectureId));
    }

    @Operation(summary = "Class 수정")
    @PutMapping("/{lecture_id}")
    public ApplicationResponse<Void> update(@Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody PutLectureReq putLectureReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.update(member, lectureId, putLectureReq));
    }

    @Operation(summary = "메인 페이지용 사용자별 Class 메타 데이터 모두 반환")
    @GetMapping("/main")
    public ApplicationResponse<List<GetLectureMainRes>> getAllMain(@Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.getAllMain(member));
    }

    @Operation(summary = "일정 페이지용 사용자별 Class 메타 데이터 모두 반환")
    @GetMapping("/calendar")
    public ApplicationResponse<List<GetLectureMetaRes>> getAllMeta(@Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.getAllMeta(member));
    }

    @Operation(summary = "Class 메인 페이지용 Class 및 Lesson 정보 반환")
    @GetMapping("/class/{lecture_id}")
    public ApplicationResponse<GetLectureRes> getLectureAndLesson(@Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.getLectureAndLesson(member, lectureId));
    }

    @Operation(summary = "학생 초대하기")
    @PostMapping("/student/invite/{lecture_id}")
    public ApplicationResponse<Void> inviteStudent(@Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody PostInviteReq postInviteReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        lectureService.inviteStudent(member, lectureId, postInviteReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "학생 가계정 생성")
    @PostMapping("/student/register/{lecture_id}")
    public ApplicationResponse<Void> registerStudent(@Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody PostStudentReq postStudentReq) {
        lectureService.registerStudent(member, lectureId, postStudentReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "학생 내보내기")
    @PostMapping("/student/emit/{lecture_id}")
    public ApplicationResponse<Void> emitStudent(@Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody List<PostStudentIdReq> postStudentIdReqList) {
        lectureService.emitStudent(member, lectureId, postStudentIdReqList);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }
}
