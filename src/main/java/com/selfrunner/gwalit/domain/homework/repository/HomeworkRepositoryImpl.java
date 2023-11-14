package com.selfrunner.gwalit.domain.homework.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkMainRes;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkRes;
import com.selfrunner.gwalit.domain.homework.dto.response.HomeworkStatisticsRes;
import com.selfrunner.gwalit.domain.homework.dto.service.HomeworkRemind;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.selfrunner.gwalit.domain.homework.entity.QHomework.homework;
import static com.selfrunner.gwalit.domain.lecture.entity.QLecture.lecture;
import static com.selfrunner.gwalit.domain.lesson.entity.QLesson.lesson;
import static com.selfrunner.gwalit.domain.member.entity.QMember.member;

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
    public Optional<List<HomeworkMainRes>> findRecentHomeworkByMemberAndLessonIdList(Member member, List<Long> lessonIdList) {
        return Optional.ofNullable(
            queryFactory.selectFrom(homework)
                    .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                    .leftJoin(lecture).on(lecture.lectureId.eq(lesson.lecture.lectureId))
                    .where(homework.lessonId.in(lessonIdList), homework.memberId.eq(member.getMemberId()), homework.deletedAt.isNull())
                    .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, lecture.lectureId, lecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)))
        );
    }

    @Override
    public Optional<List<HomeworkMainRes>> findAllHomeworkByMember(Member member) {
        return Optional.ofNullable(
            queryFactory.selectFrom(homework)
                    .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                    .leftJoin(lecture).on(lecture.lectureId.eq(lesson.lecture.lectureId))
                    .where(homework.memberId.eq(member.getMemberId()), homework.deletedAt.isNull())
                    .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, lecture.lectureId, lecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)))
        );
    }

    @Override
    public Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndType(Member member, Boolean type) {
        return Optional.ofNullable(
                queryFactory.selectFrom(homework)
                        .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                        .leftJoin(lecture).on(lecture.lectureId.eq(lesson.lecture.lectureId))
                        .where(homework.memberId.eq(member.getMemberId()), homework.isFinish.eq(type), homework.deletedAt.isNull())
                        .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, lecture.lectureId, lecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)))
        );
    }

    @Override
    public HomeworkMainRes findHomeworkByHomeworkId(Long homeworkId) {
        return queryFactory.select(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, lecture.lectureId, lecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish))
                .from(homework)
                .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                .leftJoin(lecture).on(lecture.lectureId.eq(lesson.lecture.lectureId))
                .where(homework.homeworkId.eq(homeworkId), homework.deletedAt.isNull())
                .fetchOne();

    }

    @Override
    public Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndLectureId(Member member, Long lectureId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(homework)
                        .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                        .leftJoin(lecture).on(lecture.lectureId.eq(lesson.lecture.lectureId))
                        .where(homework.memberId.eq(member.getMemberId()), homework.deletedAt.isNull(), lecture.lectureId.eq(lectureId))
                        .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, lecture.lectureId, lecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)))
        );
    }

    @Override
    public Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndLectureIdAndType(Member member, Long lectureId, Boolean type) {
        return Optional.ofNullable(
                queryFactory.selectFrom(homework)
                        .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                        .leftJoin(lecture).on(lecture.lectureId.eq(lesson.lecture.lectureId))
                        .where(homework.memberId.eq(member.getMemberId()), homework.isFinish.eq(type), homework.deletedAt.isNull(), lecture.lectureId.eq(lectureId))
                        .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, lecture.lectureId, lecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)))
        );
    }

    @Override
    public void deleteAllByLessonIdAndMemberIdList(Long lessonId, List<Long> deleteIdList) {
        queryFactory.update(homework)
                .set(homework.deletedAt, LocalDateTime.now())
                .where(homework.lessonId.eq(lessonId), homework.memberId.in(deleteIdList))
                .execute();
    }

    @Override
    public List<HomeworkStatisticsRes> findAllByBodyAndCreatedAt(Long memberId, Long lessonId, String body, LocalDate deadline, LocalDateTime createdAt) {
        return queryFactory.selectFrom(homework)
                .leftJoin(member).on(member.memberId.eq(homework.memberId))
                .where(homework.memberId.ne(memberId), homework.lessonId.eq(lessonId), homework.body.eq(body), homework.deadline.eq(deadline), homework.createdAt.eq(createdAt), member.deletedAt.isNull(), member.state.ne(MemberState.FAKE))
                .transform(groupBy(homework.memberId).list(Projections.constructor(HomeworkStatisticsRes.class, homework.homeworkId, homework.memberId, member.name, homework.lessonId, homework.body, homework.deadline, homework.isFinish)));
    }

    @Override
    public List<HomeworkRemind> findHomeworkByIsFinish(List<Long> homeworkIdList) {
        return queryFactory.selectFrom(homework)
                .leftJoin(lesson).on(homework.lessonId.eq(lesson.lessonId))
                .leftJoin(lecture).on(lesson.lecture.lectureId.eq(lesson.lessonId))
                .where(lesson.deletedAt.isNull(), lecture.deletedAt.isNull(), homework.homeworkId.in(homeworkIdList), homework.isFinish.eq(Boolean.FALSE), member.token.isNotNull())
                .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkRemind.class, homework.homeworkId, lecture.lectureId, lecture.name, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)));
    }
}
