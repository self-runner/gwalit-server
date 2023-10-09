package com.selfrunner.gwalit.domain.workbook.repository;

import com.querydsl.core.QueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkbookRepositoryImpl implements WorkbookRepositoryCustom{

    private final QueryFactory queryFactory;
}
