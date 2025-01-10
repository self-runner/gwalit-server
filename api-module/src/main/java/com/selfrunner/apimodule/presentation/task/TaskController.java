package com.selfrunner.apimodule.presentation.task;

import com.selfrunner.apimodule.global.auth.Auth;
import com.selfrunner.commonmodule.common.ApplicationResponse;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.commonmodule.dto.task.request.PostTaskReq;
import com.selfrunner.commonmodule.dto.task.request.PutTaskReq;
import com.selfrunner.commonmodule.dto.task.response.TaskRes;
import com.selfrunner.apimodule.application.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
@Tag(name = "Task", description = "할 일 관련")
public class TaskController {

    private final TaskService taskService;

    @Operation(description = "할 일 생성")
    @PostMapping("")
    public ApplicationResponse<TaskRes> register(@Auth Member member, @Valid @RequestBody PostTaskReq postTaskReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, taskService.register(member, postTaskReq));
    }

    @Operation(description = "할 일 수정")
    @PutMapping("/{task_id}")
    public ApplicationResponse<TaskRes> update(@Auth Member member, @PathVariable("task_id") Long taskId, @Valid @RequestBody PutTaskReq putTaskReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, taskService.update(member, taskId, putTaskReq));
    }

    @Operation(description = "할 일 삭제")
    @DeleteMapping("/{task_id}")
    public ApplicationResponse<Void> delete(@Auth Member member, @PathVariable("task_id") Long taskId) {
        taskService.delete(member, taskId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(description = "유저의 할 일 가져오기")
    @GetMapping("/main")
    public ApplicationResponse<List<TaskRes>> getTasksByUser(@Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, taskService.getTasksByUser(member));
    }

    @Operation(description = "Class별 할 일 가져오기")
    @GetMapping("/lecture/{lecture_id}")
    public ApplicationResponse<List<TaskRes>> getTasksByLecture(@Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, taskService.getTasksByLecture(member, lectureId));
    }
}
