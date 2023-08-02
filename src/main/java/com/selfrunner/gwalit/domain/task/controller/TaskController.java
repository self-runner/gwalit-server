package com.selfrunner.gwalit.domain.task.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Tag(name = "Task", description = "할 일 관련")
public class TaskController {
}
