package com.selfrunner.gwalit.domain.homework.controller;

import com.selfrunner.gwalit.domain.homework.service.HomeworkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/homework")
@Tag(name = "Homework", description = "숙제 관련")
public class HomeworkController {

    private final HomeworkService homeworkService;
}
