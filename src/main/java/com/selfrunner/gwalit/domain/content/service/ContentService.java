package com.selfrunner.gwalit.domain.content.service;

import com.selfrunner.gwalit.domain.content.dto.request.ContentReq;
import com.selfrunner.gwalit.domain.content.dto.request.PutContentReq;
import com.selfrunner.gwalit.domain.content.dto.response.ContentRes;
import com.selfrunner.gwalit.domain.content.entity.Content;
import com.selfrunner.gwalit.domain.content.repository.ContentRepository;
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
            throw new RuntimeException("이미 존재하는 콘텐츠입니다.");
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
                .map(ContentRes::staticToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ContentRes update(Long contentId, ContentReq contentReq) {
        // Validation
        Content content = contentRepository.findById(contentId).orElseThrow();
        if(contentRepository.existsByLinkUrl(contentReq.getLinkUrl())) {
            throw new RuntimeException("이미 존재하는 콘텐츠입니다.");
        }

        // Business Logic
        content.update(contentReq.toEntity());

        // Response
        ContentRes contentRes = new ContentRes().toDto(content);
        return contentRes;
    }
}
