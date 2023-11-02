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
@RequestMapping("")
@Tag(name = "Content", description = "교육 콘텐츠 관련")
public class ContentController {

    private final ContentService contentService;

    @PostMapping({"/content", "/api/v{version}/content"})
    public ApplicationResponse<ContentRes> register(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @Valid @RequestPart(value = "data") ContentReq contentReq, @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailImage) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, contentService.register(member, contentReq, thumbnailImage));
    }

    @PutMapping({"/content/{content_id}", "/api/v{version}/content/{content_id}"})
    public ApplicationResponse<ContentRes> update(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("content_id") Long contentId, @Valid @RequestPart(value = "data") ContentReq contentReq, @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailImage) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, contentService.update(member, contentId, contentReq, thumbnailImage));
    }

    @PatchMapping({"/content/{content_id}", "/api/v{version}/content/{content_id}"})
    public ApplicationResponse<ContentRes> updateIsPinned(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("content_id") Long contentId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, contentService.updateIsPinned(member, contentId));
    }

    @DeleteMapping({"/content/{content_id}", "/api/v{version}/content/{content_id}"})
    public ApplicationResponse<Void> delete(@PathVariable(name = "version", required = false) Long version, @Auth Member member, @PathVariable("content_id") Long contentId) {
        contentService.delete(member, contentId);
        return ApplicationResponse.ok(ErrorCode.SUCCESS);
    }

    @Operation(description = "고정 설정이 된 칼럼만 반환")
    @GetMapping({"/content", "/api/v{version}/content"})
    public ApplicationResponse<List<ContentRes>> getAll(@PathVariable(name = "version", required = false) Long version) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, contentService.getAll());
    }

}
