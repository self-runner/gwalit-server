package com.selfrunner.gwalit.domain.board.repository;

import com.selfrunner.gwalit.domain.board.dto.response.ReplyRes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

public interface ReplyRepositoryCustom {

    // 게시글의 총 댓글 수 조회
    Integer findReplyCountByBoardId(Long boardId);

    // 댓글 페이지네이션
    Slice<ReplyRes> findReplyPaginationByBoardId(Long boardId, Long cursor, LocalDateTime cursorCreatedAt, Pageable pageable);
}
