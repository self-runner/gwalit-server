package com.selfrunner.gwalit.domain.content.controller;

import com.selfrunner.gwalit.domain.content.dto.request.ContentReq;
import com.selfrunner.gwalit.domain.content.dto.response.ContentRes;
import com.selfrunner.gwalit.domain.content.service.ContentService;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/content")
@Tag(name = "Content", description = "교육 콘텐츠 관련")
public class ContentController {

    private final ContentService contentService;

    @PostMapping("")
    public ApplicationResponse<ContentRes> register(@Auth Member member, @Valid @RequestPart(value = "data") ContentReq contentReq, @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailImage) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, contentService.register(member, contentReq, thumbnailImage));
    }

    @PutMapping("/{content_id}")
    public ApplicationResponse<ContentRes> update(@Auth Member member, @PathVariable("content_id") Long contentId, @Valid @RequestPart(value = "data") ContentReq contentReq, @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailImage) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, contentService.update(member, contentId, contentReq, thumbnailImage));
    }

    @PatchMapping("/{content_id}")
    public ApplicationResponse<ContentRes> updateIsPinned(@Auth Member member, @PathVariable("content_id") Long contentId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, contentService.updateIsPinned(member, contentId));
    }

    @DeleteMapping("/{content_id}")
    public ApplicationResponse<Void> delete(@Auth Member member, @PathVariable("content_id") Long contentId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, contentService.delete(member, contentId));
    }

    @Operation(description = "고정 설정이 된 칼럼만 반환")
    @GetMapping("")
    public ApplicationResponse<List<ContentRes>> getAll() {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, contentService.getAll());
    }

}
