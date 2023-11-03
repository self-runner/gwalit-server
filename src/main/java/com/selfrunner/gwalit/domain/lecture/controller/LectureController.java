package com.selfrunner.gwalit.domain.lecture.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.selfrunner.gwalit.domain.lecture.dto.request.*;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMainRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMetaRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetStudentRes;
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
@RequestMapping("")
@Tag(name = "Lecture", description = "Class 기본 정보 관련")
public class LectureController {

    private final LectureService lectureService;

    @Operation(summary = "Class 생성")
    @PostMapping({"/lecture", "/api/v{version}/lecture"})
    public ApplicationResponse<GetLectureMetaRes> register(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @Valid @RequestBody PostLectureReq postLectureReq) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, lectureService.register(member, postLectureReq));
    }

    @Operation(summary = "Class 삭제")
    @DeleteMapping({"/lecture/{lecture_id}", "/api/v{version}/lecture/{lecture_id}"})
    public ApplicationResponse<Void> delete(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        lectureService.delete(member, lectureId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "특정 Class 정보 반환")
    @GetMapping({"/lecture/{lecture_id}", "/api/v{version}/lecture/{lecture_id}"})
    public ApplicationResponse<GetLectureMetaRes> get(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.get(member, lectureId));
    }

    @Operation(summary = "Class 수정")
    @PutMapping({"/lecture/{lecture_id}", "/api/v{version}/lecture/{lecture_id}"})
    public ApplicationResponse<GetLectureMetaRes> update(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody PutLectureReq putLectureReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.update(member, lectureId, putLectureReq));
    }

    @Operation(summary = "색상 수정")
    @PatchMapping({"/lecture/{lecture_id}/color", "/api/v{version}/lecture/{lecture_id}/color"})
    public ApplicationResponse<GetLectureMainRes> updateColor(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody PatchColorReq patchColorReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.updateColor(member, lectureId, patchColorReq));
    }

    @Operation(summary = "이름 수정")
    @PatchMapping({"/lecture/{lecture_id}/name", "/api/v{version}/lecture/{lecture_id}/name"})
    public ApplicationResponse<GetLectureMainRes> updateName(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody PatchNameReq patchNameReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.updateName(member, lectureId, patchNameReq));
    }

    @Operation(summary = "메인 페이지용 사용자별 Class 메타 데이터 모두 반환")
    @GetMapping({"/lecture/main", "/api/v{version}/lecture/main"})
    public ApplicationResponse<List<GetLectureMainRes>> getAllMain(@PathVariable(name = "version", required = false) Long version, @Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.getAllMain(member));
    }

    @Operation(summary = "일정 페이지용 사용자별 Class 메타 데이터 모두 반환")
    @GetMapping({"/lecture/calendar", "/api/v{version}/lecture/calendar"})
    public ApplicationResponse<List<GetLectureMetaRes>> getAllMeta(@PathVariable(name = "version", required = false) Long version, @Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.getAllMeta(member));
    }

    @Operation(summary = "Class 메인 페이지용 Class 및 Lesson 정보 반환")
    @GetMapping({"/lecture/class/{lecture_id}", "/api/v{version}/lecture/class/{lecture_id}"})
    public ApplicationResponse<GetLectureRes> getLectureAndLesson(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.getLectureAndLesson(member, lectureId));
    }


    @Operation(summary = "학생 초대하기")
    @PostMapping({"/lecture/student/invite/{lecture_id}", "/api/v{version}/lecture/student/invite/{lecture_id}"})
    public ApplicationResponse<Void> inviteStudent(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody PostInviteReq postInviteReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        lectureService.inviteStudent(member, lectureId, postInviteReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "학생 가계정 생성")
    @PostMapping({"/lecture/student/register/{lecture_id}", "/api/v{version}/lecture/student/register/{lecture_id}"})
    public ApplicationResponse<Void> registerStudent(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody PostStudentReq postStudentReq) {
        lectureService.registerStudent(member, lectureId, postStudentReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "학생 내보내기")
    @PostMapping({"/lecture/student/emit/{lecture_id}", "/api/v{version}/lecture/stdudent/emit/{lecture_id}"})
    public ApplicationResponse<Void> emitStudent(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId, @Valid @RequestBody List<PostStudentIdReq> postStudentIdReqList) {
        lectureService.emitStudent(member, lectureId, postStudentIdReqList);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "학생 관리")
    @GetMapping({"/lecture/student/list/{lecture_id}", "/api/v{version}/lecture/student/list/{lecture_id}"})
    public ApplicationResponse<List<GetStudentRes>> getStudent(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, lectureService.getStudent(member, lectureId));
    }
}
