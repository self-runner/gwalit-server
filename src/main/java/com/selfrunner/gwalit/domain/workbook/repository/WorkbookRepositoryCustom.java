package com.selfrunner.gwalit.domain.workbook.repository;

import com.selfrunner.gwalit.domain.workbook.dto.response.WorkbookCardRes;
import com.selfrunner.gwalit.domain.workbook.entity.SubjectDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkbookRepositoryCustom {

    // 가장 최신 자료 순으로 4개만 조회하는 쿼리
    Optional<List<WorkbookCardRes>> findAllByCreatedAtDescAndLimit(Long limit);

    // 무한 스크롤을 위한 페이지네이션
    Slice<WorkbookCardRes> findWorkbookCardPageableBy(SubjectDetail subjectDetail, String type, Long cursor, LocalDateTime cursorCreatedAt, Pageable pageable);
}
