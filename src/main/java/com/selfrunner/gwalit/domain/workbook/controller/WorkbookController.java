package com.selfrunner.gwalit.domain.workbook.controller;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.workbook.dto.request.PostProblemReq;
import com.selfrunner.gwalit.domain.workbook.service.WorkbookService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workbook")
@Tag(name = "WorkbookAndProblem", description = "문제 제작 & 문제집 제작 및 풀이 API")
public class WorkbookController {

    private final WorkbookService workbookService;

    @PostMapping("/problem")
    public ApplicationResponse<Void> register(@Auth Member member, @Valid @RequestBody PostProblemReq postProblemReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, workbookService.register(member, postProblemReq));
    }
}
