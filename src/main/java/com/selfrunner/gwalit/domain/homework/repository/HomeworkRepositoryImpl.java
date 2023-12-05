package com.selfrunner.gwalit.domain.homework.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
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
import static com.selfrunner.gwalit.domain.member.entity.QMemberAndLecture.memberAndLecture;

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
                    .leftJoin(memberAndLecture).on(memberAndLecture.lecture.lectureId.eq(lesson.lecture.lectureId))
                    .where(homework.lessonId.in(lessonIdList), homework.memberId.eq(member.getMemberId()), homework.deletedAt.isNull())
                    .orderBy(homework.homeworkId.asc(), new CaseBuilder()
                            .when(memberAndLecture.member.memberId.eq(member.getMemberId())).then(0)
                            .otherwise(1).asc())
                    .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, memberAndLecture.lecture.lectureId, memberAndLecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)))
        );
    }

    @Override
    public Optional<List<HomeworkMainRes>> findAllHomeworkByMember(Member member) {
        return Optional.ofNullable(
            queryFactory.selectFrom(homework)
                    .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                    .leftJoin(memberAndLecture).on(memberAndLecture.lecture.lectureId.eq(lesson.lecture.lectureId))
                    .where(homework.memberId.eq(member.getMemberId()), homework.deletedAt.isNull())
                    .orderBy(homework.homeworkId.asc(), new CaseBuilder()
                            .when(memberAndLecture.member.memberId.eq(member.getMemberId())).then(0)
                            .otherwise(1).asc())
                    .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, memberAndLecture.lecture.lectureId, memberAndLecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)))
        );
    }

    @Override
    public Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndType(Member member, Boolean type) {
        return Optional.ofNullable(
                queryFactory.selectFrom(homework)
                        .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                        .leftJoin(memberAndLecture).on(memberAndLecture.lecture.lectureId.eq(lesson.lecture.lectureId))
                        .where(homework.memberId.eq(member.getMemberId()), homework.isFinish.eq(type), homework.deletedAt.isNull())
                        .orderBy(homework.homeworkId.asc(), new CaseBuilder()
                                .when(memberAndLecture.member.memberId.eq(member.getMemberId())).then(0)
                                .otherwise(1).asc())
                        .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, memberAndLecture.lecture.lectureId, memberAndLecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)))
        );
    }

    @Override
    public HomeworkMainRes findHomeworkByHomeworkId(Member member, Long homeworkId) {
        return queryFactory.select(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, memberAndLecture.lecture.lectureId, memberAndLecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish))
                .from(homework)
                .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                .leftJoin(memberAndLecture).on(memberAndLecture.lecture.lectureId.eq(lesson.lecture.lectureId))
                .where(homework.homeworkId.eq(homeworkId), homework.deletedAt.isNull())
                .orderBy(homework.homeworkId.asc(), new CaseBuilder()
                        .when(memberAndLecture.member.memberId.eq(member.getMemberId())).then(0)
                        .otherwise(1).asc())
                .limit(1)
                .fetchOne();

    }

    @Override
    public Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndLectureId(Member member, Long lectureId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(homework)
                        .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                        .leftJoin(memberAndLecture).on(memberAndLecture.lecture.lectureId.eq(lesson.lecture.lectureId))
                        .where(homework.memberId.eq(member.getMemberId()), homework.deletedAt.isNull(), memberAndLecture.lecture.lectureId.eq(lectureId))
                        .orderBy(homework.homeworkId.asc(), new CaseBuilder()
                                .when(memberAndLecture.member.memberId.eq(member.getMemberId())).then(0)
                                .otherwise(1).asc())
                        .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, memberAndLecture.lecture.lectureId, memberAndLecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)))
        );
    }

    @Override
    public Optional<List<HomeworkMainRes>> findAllHomeworkByMemberAndLectureIdAndType(Member member, Long lectureId, Boolean type) {
        return Optional.ofNullable(
                queryFactory.selectFrom(homework)
                        .leftJoin(lesson).on(lesson.lessonId.eq(homework.lessonId))
                        .leftJoin(memberAndLecture).on(memberAndLecture.lecture.lectureId.eq(lesson.lecture.lectureId))
                        .where(homework.memberId.eq(member.getMemberId()), homework.isFinish.eq(type), homework.deletedAt.isNull(), memberAndLecture.lecture.lectureId.eq(lectureId))
                        .orderBy(homework.homeworkId.asc(), new CaseBuilder()
                                .when(memberAndLecture.member.memberId.eq(member.getMemberId())).then(0)
                                .otherwise(1).asc())
                        .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkMainRes.class, homework.homeworkId, memberAndLecture.lecture.lectureId, memberAndLecture.color, lesson.lessonId, homework.memberId, homework.body, homework.deadline, homework.isFinish)))
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
                .where(homework.memberId.ne(memberId), homework.lessonId.eq(lessonId), homework.body.eq(body), homework.deadline.eq(deadline), homework.createdAt.eq(createdAt), member.deletedAt.isNull(), member.state.ne(MemberState.FAKE), member.state.ne(MemberState.INVITE))
                .transform(groupBy(homework.memberId).list(Projections.constructor(HomeworkStatisticsRes.class, homework.homeworkId, homework.memberId, member.name, homework.lessonId, homework.body, homework.deadline, homework.isFinish)));
    }

    @Override
    public List<HomeworkRemind> findHomeworkByIsFinish(List<Long> homeworkIdList) {
        return queryFactory.selectFrom(homework)
                .leftJoin(lesson).on(homework.lessonId.eq(lesson.lessonId))
                .leftJoin(memberAndLecture).on(lesson.lecture.lectureId.eq(memberAndLecture.lecture.lectureId).and(homework.memberId.eq(memberAndLecture.member.memberId)))
                .leftJoin(member).on(homework.memberId.eq(member.memberId))
                .where(lesson.deletedAt.isNull(), memberAndLecture.deletedAt.isNull(), homework.homeworkId.in(homeworkIdList), homework.isFinish.eq(Boolean.FALSE), member.token.isNotNull())
                .transform(groupBy(homework.homeworkId).list(Projections.constructor(HomeworkRemind.class, homework.homeworkId, memberAndLecture.lecture.lectureId, memberAndLecture.name, lesson.lessonId, homework.memberId, member.token, homework.body, homework.deadline, homework.isFinish)));
    }
}
