package com.selfrunner.gwalit.domain.question.controller;

import com.selfrunner.gwalit.domain.question.service.QuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/question")
@Tag(name = "Question", description = "질문 게시판 기능 API")
public class QuestionController {

    private final QuestionService questionService;
}
