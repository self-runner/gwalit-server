package com.selfrunner.gwalit.domain.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.board.dto.response.FileRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.selfrunner.gwalit.domain.board.entity.QFile.file;

@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findUrlListByBoardId(Long boardId) {
        return null;
    }

    @Override
    public Optional<List<FileRes>> findAllByBoardId(Long boardId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(file)
                        .where(file.boardId.eq(boardId))
                        .transform(groupBy(file.url).list(Projections.constructor(FileRes.class, file.name, file.url, file.size)))
        );
    }
}
