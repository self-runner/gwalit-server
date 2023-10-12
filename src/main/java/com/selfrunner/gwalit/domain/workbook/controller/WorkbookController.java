package com.selfrunner.gwalit.domain.workbook.controller;

import com.selfrunner.gwalit.domain.member.entity.Member;

import com.selfrunner.gwalit.domain.workbook.dto.request.WorkbookReq;
import com.selfrunner.gwalit.domain.workbook.dto.response.*;
import com.selfrunner.gwalit.domain.workbook.service.WorkbookService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workbook")
@Tag(name = "WorkbookAndProblem", description = "문제 제작 & 문제집 제작 및 풀이 API")
public class WorkbookController {

    private final WorkbookService workbookService;

    @Operation(summary = "문제집 등록")
    @PostMapping("")
    public ApplicationResponse<WorkbookRes> registerWorkbook(@Auth Member member, @Valid @RequestPart(value = "data") WorkbookReq workbookReq, @RequestPart(value = "thumbnail") MultipartFile thumbnailImage, @RequestPart(value = "workbook") MultipartFile workbookFile, @RequestPart(value = "answer") MultipartFile answerFile) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.registerWorkbook(member, workbookReq, thumbnailImage, workbookFile, answerFile));
    }

    @Operation(summary = "문제집 수정")
    @PutMapping("/{workbook_id}")
    public ApplicationResponse<WorkbookRes> updateWorkbook(@Auth Member member, @PathVariable("workbook_id") Long workbookId, @Valid @RequestPart(value = "data") WorkbookReq workbookReq, @RequestPart(value = "thumbnail") MultipartFile thumbnailImage, @RequestPart(value = "workbook") MultipartFile workbookFile, @RequestPart(value = "answer") MultipartFile answerFile) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.updateWorkbook(member, workbookId, workbookReq, thumbnailImage, workbookFile, answerFile));
    }

    @Operation(summary = "문제집 삭제")
    @DeleteMapping("/{workbook_id}")
    public ApplicationResponse<Void> deleteWorkbook(@Auth Member member, @PathVariable("workbook_id") Long workbookId) {
        workbookService.deleteWorkbook(member, workbookId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "특정 문제집 반환")
    @GetMapping("/{workbook_id}")
    public ApplicationResponse<WorkbookRes> getOneWorkbook(@Auth Member member, @PathVariable("workbook_id") Long workbookId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.getOneWorkbook(member, workbookId));
    }

    @Operation(summary = "콘텐츠 메인 페이지의 최신 문제집 리스트 반환")
    @GetMapping("/main")
    public ApplicationResponse<List<WorkbookCardRes>> getMainWorkbookList(@Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.getMainWorkbookList(member));
    }

    @Operation(summary = "문제집 리스트 페이지네이션")
    @GetMapping("")
    public ApplicationResponse<Slice<WorkbookCardRes>> getWorkbookList(@Auth Member member, @RequestParam(name = "detail") String subjectDetail, @RequestParam(name = "type") String type, @RequestParam(name = "cursor") Long cursor, @PageableDefault(size = 10, sort = "created_at DESC")Pageable pageable) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.getWorkbookList(member, subjectDetail, type, cursor, pageable));
    }
}
