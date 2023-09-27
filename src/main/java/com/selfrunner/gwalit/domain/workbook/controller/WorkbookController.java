package com.selfrunner.gwalit.domain.workbook.controller;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.workbook.dto.request.PostProblemReq;
import com.selfrunner.gwalit.domain.workbook.dto.response.PostProblemRes;
import com.selfrunner.gwalit.domain.workbook.service.WorkbookService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workbook")
@Tag(name = "WorkbookAndProblem", description = "문제 제작 & 문제집 제작 및 풀이 API")
public class WorkbookController {

    private final WorkbookService workbookService;

    @PostMapping("/problem")
    public ApplicationResponse<PostProblemRes> register(@Auth Member member, @Valid @RequestPart(value = "data") PostProblemReq postProblemReq, @RequestPart(value = "problem") MultipartFile problemFile, @RequestPart(value = "solve") MultipartFile solveFile) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.registerProblem(member, postProblemReq, problemFile, solveFile));
    }

//    @PutMapping("/problem/{problem_id}")
//    public ApplicationResponse<Void> update(@Auth Member member) {
//        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.update(member));
//    }
}
