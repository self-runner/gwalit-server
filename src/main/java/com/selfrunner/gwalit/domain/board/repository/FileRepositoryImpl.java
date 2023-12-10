package com.selfrunner.gwalit.domain.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findUrlListByBoardId(Long boardId) {
        return null;
    }
}
