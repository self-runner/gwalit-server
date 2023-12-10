package com.selfrunner.gwalit.domain.board.repository;

import java.util.List;

public interface FileRepositoryCustom {

    List<String> findUrlListByBoardId(Long boardId);
}
