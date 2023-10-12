package com.selfrunner.gwalit.domain.workbook.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.workbook.dto.response.WorkbookCardRes;
import com.selfrunner.gwalit.domain.workbook.entity.SubjectDetail;
import com.selfrunner.gwalit.domain.workbook.entity.WorkbookType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.selfrunner.gwalit.domain.workbook.entity.QViews.views;
import static com.selfrunner.gwalit.domain.workbook.entity.QWorkbook.workbook;

@Repository
@RequiredArgsConstructor
public class WorkbookRepositoryImpl implements WorkbookRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<WorkbookCardRes>> findAllByCreatedAtDescAndLimit(Long limit) {
        return Optional.ofNullable(
            queryFactory.selectFrom(workbook)
                    .leftJoin(views).on(workbook.views.viewsId.eq(views.viewsId))
                    .where(workbook.deletedAt.isNull())
                    .orderBy(workbook.createdAt.desc())
                    .limit(limit)
                    .transform(groupBy(workbook.workbookId).list(Projections.constructor(WorkbookCardRes.class, workbook.workbookId, workbook.title, workbook.type, workbook.thumbnailUrl, workbook.problemCount, workbook.time, workbook.provider, views.count)))
        );
    }

    @Override
    public Slice<WorkbookCardRes> findWorkbookCardPageableBy(String subjectDetail, String type, Long cursor, Pageable pageable) {
        List<WorkbookCardRes> content = queryFactory.select(Projections.constructor(WorkbookCardRes.class, workbook.workbookId, workbook.title, workbook.type, workbook.thumbnailUrl, workbook.problemCount, workbook.time, workbook.provider, views.count))
                .from(workbook)
                .leftJoin(views).on(workbook.views.viewsId.eq(views.viewsId))
                .where(workbook.subjectDetail.eq(SubjectDetail.valueOf(subjectDetail)), workbook.type.eq(WorkbookType.valueOf(type)), eqCursorId(cursor))
                .orderBy(workbook.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        // 다음 페이지 존재 여부 계산
        Boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    // 커서기반 페이지네이션에서, 커서 뒤인지 확인하는 메소드
    private BooleanExpression eqCursorId(Long cursorId) {
        if (cursorId != null) {
            return workbook.workbookId.gt(cursorId);
        }
        return null;
    }
}
