package com.selfrunner.gwalit.domain.board.repository;

import com.selfrunner.gwalit.domain.board.dto.response.ReplyRes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReplyRepositoryCustom {

    Optional<List<ReplyRes>> findRecentReplyByBoardId(Long boardId);

    Slice<ReplyRes> findReplyPaginationByBoardId(Long boardId, Long cursor, LocalDateTime cursorCreatedAt, Pageable pageable);
}
