package com.selfrunner.gwalit.domain.content.controller;

import com.selfrunner.gwalit.domain.content.dto.request.ContentReq;
import com.selfrunner.gwalit.domain.content.dto.request.PutContentReq;
import com.selfrunner.gwalit.domain.content.dto.response.ContentRes;
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
    public ApplicationResponse<Void> register(@Valid @RequestBody ContentReq contentReq, Errors errors) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, contentService.register(contentReq));
    }

    @GetMapping("")
    public ApplicationResponse<List<ContentRes>> getAll() {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, contentService.getAll());
    }

    @PutMapping("/{content_id}")
    public ApplicationResponse<ContentRes> update(@PathVariable("content_id") Long contentId, @Valid @RequestBody ContentReq contentReq) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, contentService.update(contentId, contentReq));
    }
}
