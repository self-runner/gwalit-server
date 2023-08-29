package com.selfrunner.gwalit.domain.homework.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.selfrunner.gwalit.domain.homework.entity.QHomework.homework;

@Repository
@RequiredArgsConstructor
public class HomeworkRepositoryImpl implements HomeworkRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public void deleteHomeworkByLessonId(Long lessonId) {
        queryFactory.update(homework)
                .set(homework.deletedAt, LocalDateTime.now())
                .where(homework.lessonId.eq(lessonId))
                .execute();
    }

    @Override
    public List<HomeworkRes> findAllByMemberIdAndLessonId(Long memberId, Long lessonId) {
        return queryFactory.selectFrom(homework)
                        .where(homework.memberId.eq(memberId), homework.lessonId.eq(lessonId))
                        .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkRes.class, homework.homeworkId, homework.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)));
    }

    @Override
    public void deleteAllByLessonIdList(List<Long> lessonIdList) {
        queryFactory.update(homework)
                .set(homework.deletedAt, LocalDateTime.now())
                .where(homework.lessonId.in(lessonIdList))
                .execute();
    }
}
