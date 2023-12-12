package com.selfrunner.gwalit.domain.board.repository;


import com.selfrunner.gwalit.domain.board.dto.response.BoardMetaRes;
import com.selfrunner.gwalit.domain.board.entity.Board;
import com.selfrunner.gwalit.domain.board.enumerate.BoardCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    // 카테고리별 게시글 페이지네이션
    Slice<BoardMetaRes> findBoardPaginationByCategory(Long memberId, BoardCategory category, Long cursor, LocalDateTime cursorCreatedAt, Pageable pageable);

    // 해당 게시글을 작성한 클래스에 소속되어 있는지를 확인하고 게시글 정보를 반환
    Optional<Board> findBoardByMemberIdAndBoardId(Long memberId, Long boardId);

    // 메인 페이지에서 미해결 질문 리스트 반환하는 API 개발
    List<BoardMetaRes> findUnsolvedBoardResByMemberId(Long memberId);
}
