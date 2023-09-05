package com.selfrunner.gwalit.domain.homework.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkMainRes;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.selfrunner.gwalit.domain.homework.entity.QHomework.homework;
import static com.selfrunner.gwalit.domain.lecture.entity.QLecture.lecture;
import static com.selfrunner.gwalit.domain.lesson.entity.QLesson.lesson;

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

    @Override
    public Optional<List<HomeworkMainRes>> findAllByMemberIdAndLessonIdList(Member member, List<Long> lessonIdList) {
        return Optional.ofNullable(
            queryFactory.selectFrom(homework)
                    .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                    .leftJoin(lecture).on(lecture.lectureId.eq(lesson.lecture.lectureId))
                    .where(homework.memberId.eq(member.getMemberId()), homework.lessonId.in(lessonIdList))
                    .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, )))
        );
    }
}
