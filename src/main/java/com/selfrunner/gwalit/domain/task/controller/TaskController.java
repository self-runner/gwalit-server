package com.selfrunner.gwalit.domain.task.controller;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.task.dto.request.PostTaskReq;
import com.selfrunner.gwalit.domain.task.dto.request.PutTaskReq;
import com.selfrunner.gwalit.domain.task.dto.response.TaskRes;
import com.selfrunner.gwalit.domain.task.service.TaskService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
@Tag(name = "Task", description = "할 일 관련")
public class TaskController {

    private final TaskService taskService;

    @Operation(description = "할 일 생성")
    @PostMapping({"/task", "/api/v{version}/task"})
    public ApplicationResponse<Void> register(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @Valid @RequestBody PostTaskReq postTaskReq) {
        taskService.register(member, postTaskReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(description = "할 일 수정")
    @PutMapping({"/task/{task_id}", "/api/v{version}/task/{task_id}"})
    public ApplicationResponse<Void> update(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("task_id") Long taskId, @Valid @RequestBody PutTaskReq putTaskReq) {
        taskService.update(member, taskId, putTaskReq);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(description = "할 일 삭제")
    @DeleteMapping({"/task/{task_id}", "/api/v{version}/task/{task_id}"})
    public ApplicationResponse<Void> delete(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("task_id") Long taskId) {
        taskService.delete(member, taskId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(description = "유저의 할 일 가져오기")
    @GetMapping({"/task/main", "/api/v{version}/task/main"})
    public ApplicationResponse<List<TaskRes>> getTasksByUser(@PathVariable(name = "version", required = false) Long version, @Auth Member member) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, taskService.getTasksByUser(member));
    }

    @Operation(description = "Class별 할 일 가져오기")
    @GetMapping({"/task/lecture/{lecture_id}", "/api/v{version}/task/lecture/{lecture_id}"})
    public ApplicationResponse<List<TaskRes>> getTasksByLecture(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, taskService.getTasksByLecture(member, lectureId));
    }
}
