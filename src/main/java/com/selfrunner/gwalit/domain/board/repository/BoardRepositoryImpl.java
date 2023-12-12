package com.selfrunner.gwalit.domain.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.board.dto.response.BoardMetaRes;
import com.selfrunner.gwalit.domain.board.entity.Board;
import com.selfrunner.gwalit.domain.board.enumerate.BoardCategory;
import com.selfrunner.gwalit.domain.board.enumerate.QuestionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.selfrunner.gwalit.domain.board.entity.QBoard.board;
import static com.selfrunner.gwalit.domain.board.entity.QReply.reply;
import static com.selfrunner.gwalit.domain.lecture.entity.QLecture.lecture;
import static com.selfrunner.gwalit.domain.member.entity.QMember.member;
import static com.selfrunner.gwalit.domain.member.entity.QMemberAndLecture.memberAndLecture;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<BoardMetaRes> findBoardPaginationByCategory(Long memberId, BoardCategory category, Long cursor, LocalDateTime cursorCreatedAt, Pageable pageable) {
        List<BoardMetaRes> content = queryFactory.selectFrom(board)
                .leftJoin(lecture).on(board.lecture.lectureId.eq(lecture.lectureId))
                .leftJoin(member).on(board.member.memberId.eq(member.memberId))
                .leftJoin(reply).on(board.boardId.eq(reply.board.boardId))
                .where(eqCursorAndCursorCreatedAt(cursor, cursorCreatedAt), board.isPublic.eq(true).or(checkWriter(memberId)))
                .orderBy(board.createdAt.desc(), board.boardId.asc())
                .groupBy(board.boardId)
                .limit(pageable.getPageSize() + 1)
                .transform(groupBy(board.boardId).list(Projections.constructor(BoardMetaRes.class, board.boardId, lecture.lectureId, member.memberId, member.type, member.name, board.lessonId, board.title, board.body, board.category, board.status, reply.count(), board.createdAt, board.modifiedAt)));

        // 다음 페이지 존재 여부 계산
        boolean hasNext = false;
        if(content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Optional<Board> findBoardByMemberIdAndBoardId(Long memberId, Long boardId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(board)
                    .leftJoin(memberAndLecture).on(board.lecture.lectureId.eq(memberAndLecture.lecture.lectureId))
                    .where(memberAndLecture.member.memberId.eq(memberId))
                    .fetchFirst()
        );
    }

    public List<BoardMetaRes> findUnsolvedBoardResByMemberId(Long memberId) {
        return queryFactory.selectFrom(board)
                .leftJoin(reply).on(reply.board.boardId.eq(board.boardId))
                .leftJoin(memberAndLecture).on(memberAndLecture.lecture.lectureId.eq(board.lecture.lectureId))
                .leftJoin(member).on(member.memberId.eq(memberAndLecture.member.memberId))
                .where(memberAndLecture.member.memberId.eq(memberId), board.status.eq(QuestionStatus.UNSOLVED), board.deletedAt.isNull())
                .groupBy(board.boardId)
                .transform(groupBy(board.boardId).list(Projections.constructor(BoardMetaRes.class, board.boardId, memberAndLecture.lecture.lectureId, member.memberId, member.type, member.name, board.lessonId, board.title, board.body, board.category, board.status, reply.board.boardId.count(), board.createdAt, board.modifiedAt)));
    }


    // 커서기반 페이지네이션에서 커서 뒤인지 확인하는 메소드
    private BooleanExpression eqCursorAndCursorCreatedAt(Long cursor, LocalDateTime cursorCreatedAt) {
        if(cursor == null || cursorCreatedAt == null) {
            return null;
        }

        return board.createdAt.lt(cursorCreatedAt)
                .or(board.boardId.gt(cursor)
                        .and(board.createdAt.eq(cursorCreatedAt)));
    }

    /**
     * 작성자 본인일 경우는 페이지네이션에서 포함되어야 함. 이를 확인하기 위한 메소드
     * @param memberId - 요청한 사용자 id
     * @return 작성자 본인이 될 경우, true 아니면 false 반환
     */
    private BooleanExpression checkWriter(Long memberId) {
        return board.member.memberId.eq(memberId);
    }
}
