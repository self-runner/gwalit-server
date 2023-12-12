package com.selfrunner.gwalit.domain.board.repository;

import com.selfrunner.gwalit.domain.board.dto.response.FileRes;

import java.util.List;
import java.util.Optional;

public interface FileRepositoryCustom {

    List<String> findUrlListByBoardId(Long boardId);

    Optional<List<FileRes>> findAllByBoardId(Long boardId);

    // 클래스별 파일 사용용량 조회 쿼리
    Long findCapacityByLectureId(Long lectureId);

    // 삭제될 파일용량들 조회 쿼리
    Long findDeleteCapacityByUrlList(List<String> urlList);
}
