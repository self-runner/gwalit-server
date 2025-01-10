package com.selfrunner.apimodule.application.content;

import com.selfrunner.commonmodule.dto.content.request.ContentReq;
import com.selfrunner.commonmodule.dto.content.response.ContentRes;
import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.content.Content;
import com.selfrunner.commonmodule.enumerate.content.ContentType;
import com.selfrunner.domainmodule.domain.content.repository.ContentRepository;
import com.selfrunner.domainmodule.domain.member.Member;
import com.selfrunner.inframodule.s3.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;
    private final S3Client s3Client;

    @Transactional
    public ContentRes register(Member member, ContentReq contentReq, MultipartFile thumbnailImage) {
        // Validation
        /**
         * TODO: 관리자 권한 확인 조건 추가 필요
         */
        if(contentRepository.existsByLinkUrl(contentReq.getLinkUrl())) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_CONTENT);
        }

        // Business Logic: 비디오인지, 노션인지 따라에 구분된 로직 진행
        switch (ContentType.valueOf(contentReq.getType())) {
            case VIDEO:
                Content videoContent = Content.builder()
                        .title(contentReq.getTitle())
                        .writer(contentReq.getWriter())
                        .type(contentReq.getType())
                        .classification(contentReq.getClassification())
                        .linkUrl(contentReq.getLinkUrl())
                        .duration(contentReq.getDuration())
                        .isPinned(contentReq.getIsPinned())
                        .build();
                Content saveVideoContent = contentRepository.save(videoContent);

                return new ContentRes(saveVideoContent.getContentId(),
                        saveVideoContent.getTitle(),
                        saveVideoContent.getWriter(),
                        saveVideoContent.getType(),
                        saveVideoContent.getClassification(),
                        saveVideoContent.getLinkUrl(),
                        saveVideoContent.getThumbnailUrl(),
                        saveVideoContent.getDuration(),
                        saveVideoContent.getIsPinned());

            case NOTION:
                String thumbnailUrl;
                try {
                    thumbnailUrl = s3Client.upload(thumbnailImage, "content");

                } catch (Exception e) {
                    throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
                }

                Content notionContent = Content.builder()
                        .title(contentReq.getTitle())
                        .writer(contentReq.getWriter())
                        .type(contentReq.getType())
                        .classification(contentReq.getClassification())
                        .linkUrl(contentReq.getLinkUrl())
                        .thumbnailUrl(thumbnailUrl)
                        .duration(contentReq.getDuration())
                        .isPinned(contentReq.getIsPinned())
                        .build();
                Content saveNotionContent = contentRepository.save(notionContent);

                return new ContentRes(saveNotionContent.getContentId(),
                        saveNotionContent.getTitle(),
                        saveNotionContent.getWriter(),
                        saveNotionContent.getType(),
                        saveNotionContent.getClassification(),
                        saveNotionContent.getLinkUrl(),
                        saveNotionContent.getThumbnailUrl(),
                        saveNotionContent.getDuration(),
                        saveNotionContent.getIsPinned());
        }

        // Response
        return null;
    }

    @Transactional
    public ContentRes update(Member member, Long contentId, ContentReq contentReq, MultipartFile thumbnailImage) {
        // Validation: 기존에 존재하는 링크를 재등록하는지 확인
        /**
         * TODO: 관리자 권한 확인 조건 추가 필요
         */
        Content content = contentRepository.findById(contentId).orElseThrow();
        if(contentRepository.existsByLinkUrl(contentReq.getLinkUrl())) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_CONTENT);
        }

        // Business Logic
        switch (ContentType.valueOf(contentReq.getType())) {
            case VIDEO:
                content.updateVideo(contentReq);
                Content updateVideoContent = contentRepository.save(content);

                return new ContentRes(updateVideoContent.getContentId(),
                        updateVideoContent.getTitle(),
                        updateVideoContent.getWriter(),
                        updateVideoContent.getType(),
                        updateVideoContent.getClassification(),
                        updateVideoContent.getLinkUrl(),
                        updateVideoContent.getThumbnailUrl(),
                        updateVideoContent.getDuration(),
                        updateVideoContent.getIsPinned());

            case NOTION:
                String thumbnailUrl;
                try {
                    s3Client.delete(content.getThumbnailUrl());
                    thumbnailUrl = s3Client.upload(thumbnailImage, "content");
                } catch (Exception e) {
                    throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
                }
                content.updateNotion(contentReq, thumbnailUrl);
                Content updateNotionContent = contentRepository.save(content);

                return new ContentRes(updateNotionContent.getContentId(),
                        updateNotionContent.getTitle(),
                        updateNotionContent.getWriter(),
                        updateNotionContent.getType(),
                        updateNotionContent.getClassification(),
                        updateNotionContent.getLinkUrl(),
                        updateNotionContent.getThumbnailUrl(),
                        updateNotionContent.getDuration(),
                        updateNotionContent.getIsPinned());
        }

        // Response
        return null;
    }

    @Transactional
    public void delete(Member member, Long contentId) {
        // Validation
        /**
         * TODO: 관리자 권한 확인 조건 추가 필요
         */
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(content.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_EXCEPTION);
        }

        // Business Logic: 썸네일 이미지 삭제 및 Row 삭제
        if(content.getType().equals(ContentType.NOTION)) {
            try {
                s3Client.delete(content.getThumbnailUrl());
            } catch (Exception e) {
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }
        contentRepository.delete(content);

        // Response
    }

    @Transactional
    public ContentRes updateIsPinned(Member member, Long contentId) {
        // Validation
        /**
         * TODO: 관리자 권한 확인 조건 추가 필요
         */
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        content.updateIsPinned();
        Content updateContent = contentRepository.save(content);

        // Response
        return new ContentRes(updateContent.getContentId(),
                updateContent.getTitle(),
                updateContent.getWriter(),
                updateContent.getType(),
                updateContent.getClassification(),
                updateContent.getLinkUrl(),
                updateContent.getThumbnailUrl(),
                updateContent.getDuration(),
                updateContent.getIsPinned());
    }

    public List<ContentRes> getAll() {
        // Validation

        // Business Logic
        List<Content> contentList = contentRepository.findAllByIsPinnedOrderByCreatedAtDesc(Boolean.TRUE).orElse(null);

        // Response
        return contentList.stream()
                .map(content -> new ContentRes(content.getContentId(),
                        content.getTitle(),
                        content.getWriter(),
                        content.getType(),
                        content.getClassification(),
                        content.getLinkUrl(),
                        content.getThumbnailUrl(),
                        content.getDuration(),
                        content.getIsPinned()))
                .collect(Collectors.toList());
    }
}
