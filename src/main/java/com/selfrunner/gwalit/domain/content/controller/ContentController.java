package com.selfrunner.gwalit.domain.content.controller;

import com.selfrunner.gwalit.domain.content.dto.request.PostContentReq;
import com.selfrunner.gwalit.domain.content.dto.response.GetContentRes;
import com.selfrunner.gwalit.domain.content.service.ContentService;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
@Tag(name = "Content", description = "교육 콘텐츠 관련")
public class ContentController {

    private final ContentService contentService;

    @PostMapping("")
    public ApplicationResponse<Void> register(@Valid @RequestBody PostContentReq postContentReq, Errors errors) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, contentService.register(postContentReq));
    }

    @GetMapping("")
    public ApplicationResponse<List<GetContentRes>> getAll() {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, contentService.getAll());
    }
}
