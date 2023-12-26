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
        return queryFactory.select(file.url)
                .from(file)
                .where(file.boardId.eq(boardId), file.deletedAt.isNull())
                .fetch();
    }

    @Override
    public List<String> findUrlListByReplyId(Long replyId) {
        return queryFactory.select(file.url)
                .from(file)
                .where(file.replyId.eq(replyId), file.deletedAt.isNull())
                .fetch();
    }

    @Override
    public Optional<List<FileRes>> findAllByBoardId(Long boardId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(file)
                        .where(file.boardId.eq(boardId), file.replyId.isNull(), file.deletedAt.isNull())
                        .transform(groupBy(file.url).list(Projections.constructor(FileRes.class, file.name, file.url, file.size)))
        );
    }

    @Override
    public Long findCapacityByLectureId(Long lectureId) {
        return queryFactory.select(file.size.sum())
                .from(file)
                .where(file.lectureId.eq(lectureId), file.deletedAt.isNull())
                .fetchFirst();
    }

    @Override
    public Long findDeleteCapacityByUrlList(List<String> urlList) {
        return queryFactory.select(file.size.sum())
                .from(file)
                .where(file.url.in(urlList), file.deletedAt.isNull())
                .fetchFirst();
    }

    @Override
    public Long findCountByBoardId(Long boardId) {
        return queryFactory.select(file.count())
                .from(file)
                .where(file.boardId.eq(boardId), file.replyId.isNull(), file.deletedAt.isNull())
                .fetchFirst();
    }
}
