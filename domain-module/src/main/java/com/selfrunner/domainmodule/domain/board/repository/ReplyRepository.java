package com.selfrunner.domainmodule.domain.board.repository;

import com.selfrunner.domainmodule.domain.board.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {

    void deleteAllByBoardBoardId(Long boardId);
}
