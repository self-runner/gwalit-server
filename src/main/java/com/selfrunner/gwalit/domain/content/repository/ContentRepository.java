package com.selfrunner.gwalit.domain.content.repository;

import com.selfrunner.gwalit.domain.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {

    // 기존에 존재하는 콘텐츠인지 조회
    Boolean existsByLinkUrl(String linkUrl);

    // 고정된 콘텐츠들 조회
    Optional<List<Content>> findAllByIsPinnedOrderByCreatedAtDesc(Boolean isPinned);
}
