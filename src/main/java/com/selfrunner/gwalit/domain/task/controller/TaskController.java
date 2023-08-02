package com.selfrunner.gwalit.domain.task.controller;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.task.dto.request.PostTaskReq;
import com.selfrunner.gwalit.domain.task.service.TaskService;
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
@RequestMapping("/task")
@Tag(name = "Task", description = "할 일 관련")
public class TaskController {

    private final TaskService taskService;

    @Operation(description = "할 일 생성")
    @PostMapping("")
    public ApplicationResponse<Void> register(@Auth Member member, @Valid @RequestBody PostTaskReq postTaskReq) {
        taskService.register(member, postTaskReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }
}
