package com.selfrunner.gwalit.domain.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.board.dto.response.FileRes;
import com.selfrunner.gwalit.domain.board.dto.response.ReplyRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.types.Projections.list;
import static com.selfrunner.gwalit.domain.board.entity.QBoard.board;
import static com.selfrunner.gwalit.domain.board.entity.QFile.file;
import static com.selfrunner.gwalit.domain.board.entity.QReply.reply;
import static com.selfrunner.gwalit.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Integer findReplyCountByBoardId(Long boardId) {
        return queryFactory.select(reply.count())
                .from(reply)
                .where(reply.board.boardId.eq(boardId), reply.deletedAt.isNull())
                .fetchFirst().intValue();
    }

    @Override
    public Optional<List<ReplyRes>> findRecentReplyByBoardId(Long boardId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(reply)
                    .leftJoin(board).on(reply.board.boardId.eq(board.boardId))
                    .leftJoin(member).on(reply.member.memberId.eq(member.memberId))
                    .leftJoin(file).on(file.replyId.eq(reply.replyId))
                    .where(reply.board.boardId.eq(boardId), reply.deletedAt.isNull())
                    .orderBy(reply.createdAt.desc())
                    .limit(10)
                    .transform(groupBy(reply.replyId).list(Projections.constructor(ReplyRes.class, reply.replyId, board.boardId, member.memberId, member.type, member.name, reply.body,
                            list(Projections.constructor(FileRes.class, file.name, file.url, file.size)), reply.createdAt, reply.modifiedAt)))
        );
    }

    public Slice<ReplyRes> findReplyPaginationByBoardId(Long boardId, Long cursor, LocalDateTime cursorCreatedAt, Pageable pageable) {
        List<ReplyRes> content = queryFactory.select(Projections.constructor(ReplyRes.class, reply.replyId, reply.board.boardId, member.memberId, member.type, member.name, reply.body,
                        list(Projections.constructor(FileRes.class, file.name, file.url, file.size)), reply.createdAt, reply.modifiedAt))
                .from(reply)
                .leftJoin(member).on(reply.member.memberId.eq(member.memberId))
                .leftJoin(file).on(reply.replyId.eq(file.replyId))
                .where(reply.board.boardId.eq(boardId), eqCursorIdAndCursorCreatedAt(cursor, cursorCreatedAt), reply.deletedAt.isNull())
                .orderBy(reply.createdAt.desc(), reply.replyId.asc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        // 다음 페이지 존재 여부 확인
        boolean hasNext = false;
        if(content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    // 커서기반 페이지네이션 커서 뒤인지 확인하는 메소드
    private BooleanExpression eqCursorIdAndCursorCreatedAt(Long cursor, LocalDateTime cursorCreatedAt) {
        if(cursor == null || cursorCreatedAt == null) {
            return null;
        }

        return reply.createdAt.lt(cursorCreatedAt)
                .or(reply.replyId.gt(cursor)
                        .and(reply.createdAt.eq(cursorCreatedAt)));
    }
}
