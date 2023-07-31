package com.selfrunner.gwalit.domain.homework.controller;

import com.selfrunner.gwalit.domain.homework.dto.request.PostHomeworkReq;
import com.selfrunner.gwalit.domain.homework.service.HomeworkService;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/homework")
@Tag(name = "Homework", description = "숙제 관련")
public class HomeworkController {

    private final HomeworkService homeworkService;

    @Operation(summary = "숙제 생성")
    @PostMapping("")
    public ApplicationResponse<Void> register(@Auth Member member, @Valid @RequestBody PostHomeworkReq postHomeworkReq) {
        homeworkService.register(member, postHomeworkReq);
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }
}
