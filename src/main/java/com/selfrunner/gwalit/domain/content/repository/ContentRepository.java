package com.selfrunner.gwalit.domain.content.repository;

import com.selfrunner.gwalit.domain.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long>, ContentRepositoryCustom {

    // 기존에 존재하는 콘텐츠인지 조회
    Boolean existsByLinkUrl(String linkUrl);
}
