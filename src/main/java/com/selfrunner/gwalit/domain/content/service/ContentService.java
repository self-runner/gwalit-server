package com.selfrunner.gwalit.domain.content.service;

import com.selfrunner.gwalit.domain.content.dto.request.ContentReq;
import com.selfrunner.gwalit.domain.content.dto.response.ContentRes;
import com.selfrunner.gwalit.domain.content.entity.Content;
import com.selfrunner.gwalit.domain.content.entity.ContentType;
import com.selfrunner.gwalit.domain.content.exception.ContentException;
import com.selfrunner.gwalit.domain.content.repository.ContentRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.aws.S3Client;
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
            throw new ContentException(ErrorCode.ALREADY_EXIST_CONTENT);
        }

        // Business Logic: 비디오인지, 노션인지 따라에 구분된 로직 진행
        switch (ContentType.valueOf(contentReq.getType())) {
            case VIDEO:
                Content videoContent = contentReq.toEntity(null);
                contentRepository.save(videoContent);

                return new ContentRes(videoContent);

            case NOTION:
                String thumbnailUrl;
                try {
                    thumbnailUrl = s3Client.upload(thumbnailImage, "content");

                } catch (Exception e) {
                    throw new ContentException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
                }

                Content notionContent = contentReq.toEntity(thumbnailUrl);
                contentRepository.save(notionContent);

                return new ContentRes(notionContent);
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

                return new ContentRes(content);

            case NOTION:
                String thumbnailUrl;
                try {
                    s3Client.delete(content.getThumbnailUrl());
                    thumbnailUrl = s3Client.upload(thumbnailImage, "content");
                } catch (Exception e) {
                    throw new ContentException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
                }
                content.updateNotion(contentReq, thumbnailUrl);

                return new ContentRes(content);
        }

        // Response
        return null;
    }

    @Transactional
    public Void delete(Member member, Long contentId) {
        // Validation
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(content.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_EXCEPTION);
        }

        // Business Logic: 썸네일 이미지 삭제 및 Row 삭제
        if(content.getType().equals(ContentType.NOTION)) {
            try {
                s3Client.delete(content.getThumbnailUrl());
            } catch (Exception e) {
                throw new ContentException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }
        contentRepository.delete(content);

        // Response
        return null;
    }

    @Transactional
    public List<ContentRes> getAll() {
        // Validation

        // Business Logic
        List<Content> contents = contentRepository.findAll();

        // Response
        return contents.stream()
                .map(ContentRes::new)
                .collect(Collectors.toList());
    }
}
