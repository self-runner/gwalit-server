package com.selfrunner.gwalit.domain.homework.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.selfrunner.gwalit.domain.homework.entity.QHomework.homework;

@Repository
@RequiredArgsConstructor
public class HomeworkRepositoryImpl implements HomeworkRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public void deleteHomeworkByLessonId(Long lessonId) {
        queryFactory.delete(homework)
                .where(homework.lessonId.eq(lessonId))
                .execute();
    }
}
