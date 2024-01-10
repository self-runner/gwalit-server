package com.selfrunner.gwalit.domain.board.repository;

import com.selfrunner.gwalit.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
}
