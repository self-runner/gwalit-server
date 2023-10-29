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
@RequestMapping("")
@Tag(name = "WorkbookAndProblem", description = "문제 제작 & 문제집 제작 및 풀이 API")
public class WorkbookController {

    private final WorkbookService workbookService;

    @Operation(summary = "문제집 등록")
    @PostMapping({"/workbook", "/api/v{version}/workbook"})
    public ApplicationResponse<WorkbookRes> registerWorkbook(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @Valid @RequestPart(value = "data") WorkbookReq workbookReq, @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailImage, @RequestPart(value = "thumbnail_card", required = false) MultipartFile thumbnailCardImage, @RequestPart(value = "workbook", required = false) MultipartFile workbookFile, @RequestPart(value = "answer", required = false) MultipartFile answerFile) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.registerWorkbook(member, workbookReq, thumbnailImage, thumbnailCardImage, workbookFile, answerFile));
    }

    @Operation(summary = "문제집 수정")
    @PutMapping({"/workbook/{workbook_id}", "/api/v{version}/workbook/{workbook_id}"})
    public ApplicationResponse<WorkbookRes> updateWorkbook(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("workbook_id") Long workbookId, @Valid @RequestPart(value = "data") WorkbookReq workbookReq, @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailImage, @RequestPart(value = "thumbnail_card", required = false) MultipartFile thumbnailCardImage, @RequestPart(value = "workbook", required = false) MultipartFile workbookFile, @RequestPart(value = "answer", required = false) MultipartFile answerFile) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.updateWorkbook(member, workbookId, workbookReq, thumbnailImage, thumbnailCardImage, workbookFile, answerFile));
    }

    @Operation(summary = "문제집 삭제")
    @DeleteMapping({"/workbook/{workbook_id}", "/api/v{version}/workbook/{workbook_id}"})
    public ApplicationResponse<Void> deleteWorkbook(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("workbook_id") Long workbookId) {
        workbookService.deleteWorkbook(member, workbookId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(summary = "특정 문제집 반환")
    @GetMapping({"/workbook/{workbook_id}", "/api/v{version}/workbook/{workbook_id}"})
    public ApplicationResponse<WorkbookRes> getOneWorkbook(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("workbook_id") Long workbookId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.getOneWorkbook(member, workbookId));
    }

    @Operation(summary = "콘텐츠 메인 페이지의 최신 문제집 리스트 반환")
    @GetMapping({"/workbook/main", "/api/v{version}/workbook/main"})
    public ApplicationResponse<List<WorkbookCardRes>> getMainWorkbookList(@PathVariable(name = "version", required = false) Long version, @Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.getMainWorkbookList(member));
    }

    @Operation(summary = "문제집 리스트 페이지네이션")
    @GetMapping({"/workbook", "/api/v{version}/workbook"})
    public ApplicationResponse<Slice<WorkbookCardRes>> getWorkbookList(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @RequestParam(name = "detail") String subjectDetail, @RequestParam(name = "type") String type, @RequestParam(name = "cursor", required = false) Long cursor, @PageableDefault(size = 10, sort = "created_at DESC")Pageable pageable) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.getWorkbookList(member, subjectDetail, type, cursor, pageable));
    }
}
