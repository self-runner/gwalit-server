package com.selfrunner.gwalit.domain.board.repository;

import com.selfrunner.gwalit.domain.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {

    void deleteAllByBoardBoardId(Long boardId);
}
