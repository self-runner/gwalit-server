package com.selfrunner.gwalit.domain.workbook.controller;

import com.selfrunner.gwalit.domain.workbook.dto.response.GetSubjectMaterialRes;
import com.selfrunner.gwalit.domain.workbook.dto.response.GetSubjectRes;
import com.selfrunner.gwalit.domain.workbook.service.SubjectService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Tag(name = "Subject", description = "콘텐츠 페이지 뷰용 API")
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "콘텐츠 페이지 과목 정보 반환")
    @GetMapping({"/subject", "/api/v{version}/subject"})
    public ApplicationResponse<List<GetSubjectRes>> getSubjectList(@PathVariable(name = "version", required = false) Long version) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, subjectService.getSubjectList());
    }

    @Operation(summary = "문제집 페이지 네비게이션 바 정보 반환")
    @GetMapping({"/subject/material", "/api/v{version}/subject"})
    public ApplicationResponse<List<GetSubjectMaterialRes>> getSubjectDetailList(@PathVariable(name = "version", required = false) Long version, @RequestParam("subject") String subject) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, subjectService.getSubjectMaterialList(subject));
    }
}
