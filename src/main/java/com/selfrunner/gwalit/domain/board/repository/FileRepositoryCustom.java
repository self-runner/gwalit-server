package com.selfrunner.gwalit.domain.board.repository;

import com.selfrunner.gwalit.domain.board.dto.response.FileRes;

import java.util.List;
import java.util.Optional;

public interface FileRepositoryCustom {

    List<String> findUrlListByBoardId(Long boardId);

    Optional<List<FileRes>> findAllByBoardId(Long boardId);
}
