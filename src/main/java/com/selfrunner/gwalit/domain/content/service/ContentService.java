package com.selfrunner.gwalit.domain.content.service;

import com.selfrunner.gwalit.domain.content.dto.request.ContentReq;
import com.selfrunner.gwalit.domain.content.dto.request.PutContentReq;
import com.selfrunner.gwalit.domain.content.dto.response.ContentRes;
import com.selfrunner.gwalit.domain.content.entity.Content;
import com.selfrunner.gwalit.domain.content.repository.ContentRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    @Transactional
    public Void register(ContentReq contentReq) {
        // Validation
        if(contentRepository.existsByLinkUrl(contentReq.getLinkUrl())) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_CONTENT);
        }

        // Business Logic
        Content content = contentReq.toEntity();
        contentRepository.save(content);

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

    @Transactional
    public ContentRes update(Long contentId, ContentReq contentReq) {
        // Validation
        Content content = contentRepository.findById(contentId).orElseThrow();
        if(contentRepository.existsByLinkUrl(contentReq.getLinkUrl())) {
            throw new ApplicationException(ErrorCode.ALREADY_EXIST_CONTENT);
        }

        // Business Logic
        content.update(contentReq.toEntity());

        // Response
        ContentRes contentRes = new ContentRes(content);
        return contentRes;
    }

    @Transactional
    public Void delete(Long contentId) {
        // Validation
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(content.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_EXCEPTION);
        }

        // Business Logic
        contentRepository.delete(content);

        // Response
        return null;
    }
}
