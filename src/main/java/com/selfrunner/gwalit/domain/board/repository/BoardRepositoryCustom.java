package com.selfrunner.gwalit.domain.board.repository;


import com.selfrunner.gwalit.domain.board.dto.response.BoardMetaRes;
import com.selfrunner.gwalit.domain.board.enumerate.BoardCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface BoardRepositoryCustom {

    Slice<BoardMetaRes> findBoardPaginationByCategory(Long memberId, BoardCategory category, Long cursor, LocalDateTime cursorCreatedAt, Pageable pageable);
}
