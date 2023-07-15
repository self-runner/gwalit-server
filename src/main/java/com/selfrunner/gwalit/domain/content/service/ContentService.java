package com.selfrunner.gwalit.domain.content.service;

import com.selfrunner.gwalit.domain.content.dto.request.PostContentReq;
import com.selfrunner.gwalit.domain.content.dto.response.GetContentRes;
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
    public Void register(PostContentReq postContentReq) {
        // Validation
        if(contentRepository.existsByLinkUrl(postContentReq.getLinkUrl())) {
            throw new RuntimeException("이미 존재하는 콘텐츠입니다.");
        }

        // Business Logic
        Content content = postContentReq.toEntity();
        contentRepository.save(content);

        // Response
        return null;
    }

    @Transactional
    public List<GetContentRes> getAll() {
        // Validation

        // Business Logic
        List<Content> contents = contentRepository.findAll();

        // Response
        return contents.stream()
                .map(GetContentRes::toDto)
                .collect(Collectors.toList());
    }
}
