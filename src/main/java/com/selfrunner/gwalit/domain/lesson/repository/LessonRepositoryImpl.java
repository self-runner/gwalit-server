package com.selfrunner.gwalit.domain.lesson.repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonProgressRes;
import com.selfrunner.gwalit.domain.lesson.entity.BatchStatus;
import com.selfrunner.gwalit.domain.lesson.entity.LessonType;
import com.selfrunner.gwalit.global.batch.dto.BatchLessonDto;
import com.selfrunner.gwalit.global.batch.dto.BatchNotificationDto;
import com.selfrunner.gwalit.global.common.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.selfrunner.gwalit.domain.lecture.entity.QLecture.lecture;
import static com.selfrunner.gwalit.domain.lesson.entity.QLesson.lesson;
import static com.selfrunner.gwalit.domain.member.entity.QMember.member;
import static com.selfrunner.gwalit.domain.member.entity.QMemberAndLecture.memberAndLecture;

@Repository
@RequiredArgsConstructor
public class LessonRepositoryImpl implements LessonRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    private StringTemplate getDateFormat(String obj) {
        return Expressions.stringTemplate("DATE_FORMAT({0}, '{1s}')", lesson.date, ConstantImpl.create(obj));
    }

    @Override
    public Optional<List<LessonMetaRes>> findAllLessonMetaByLectureId(Long lectureId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(lesson)
                        .leftJoin(lecture).on(lesson.lecture.eq(lecture))
                        .where(lesson.lecture.lectureId.eq(lectureId))
                        .orderBy(lesson.date.asc(), lesson.startTime.asc(), lesson.endTime.asc())
                        .transform(groupBy(lesson.lessonId).list(Projections.constructor(LessonMetaRes.class, lesson.lessonId, lesson.lecture.lectureId, lesson.type, lesson.date, Projections.constructor(Schedule.class, lesson.weekday, lesson.startTime, lesson.endTime), lesson.participants)))
        );
    }

    @Override
    public Optional<List<LessonMetaRes>> findAllLessonMetaByYearMonth(List<Long> lectureIdList, String year, String month) {
        StringTemplate yearDateFormat = getDateFormat("%Y");
        StringTemplate monthDateFormat = getDateFormat("%m");

        return Optional.ofNullable(
                queryFactory.selectFrom(lesson)
                        .leftJoin(lecture).on(lesson.lecture.eq(lecture))
                        .where(lesson.lecture.lectureId.in(lectureIdList), yearDateFormat.eq(year), monthDateFormat.eq(month))
                        .orderBy(lesson.date.asc(), lesson.startTime.asc(), lesson.endTime.asc())
                        .transform(groupBy(lesson.lessonId).list(Projections.constructor(LessonMetaRes.class, lesson.lessonId, lesson.lecture.lectureId, lesson.type, lesson.date, Projections.constructor(Schedule.class, lesson.weekday, lesson.startTime, lesson.endTime), lesson.participants)))
        );
    }

    @Override
    public Optional<List<LessonProgressRes>> findAllProgressByLectureId(Long lectureId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(lesson)
                        .where(lesson.lecture.lectureId.eq(lectureId))
                        .transform(groupBy(lesson.lessonId).list(Projections.constructor(LessonProgressRes.class, lesson.lessonId, lesson.lecture.lectureId, lesson.date, Projections.constructor(Schedule.class, lesson.weekday, lesson.startTime, lesson.endTime), lesson.progresses)))
        );
    }

    @Override
    public List<Long> findAllLessonIdByLectureId(Long lectureId) {
        return queryFactory.select(lesson.lessonId)
                .from(lesson)
                .innerJoin(lesson).on(lesson.lecture.lectureId.eq(lectureId)).fetchJoin()
                .where(lesson.lecture.lectureId.eq(lectureId))
                .fetch();
    }

    @Override
    public Optional<LessonMetaRes> findLessonMetaByLectureIdBeforeNow(Long lectureId) {
        return Optional.ofNullable(queryFactory.select(Projections.constructor(LessonMetaRes.class, lesson.lessonId, lesson.lecture.lectureId, lesson.type, lesson.date, Projections.constructor(Schedule.class, lesson.weekday, lesson.startTime, lesson.endTime), lesson.participants))
                .from(lesson)
                .where(lesson.lecture.lectureId.eq(lectureId), lesson.date.before(LocalDate.now().plusDays(1L)))
                .orderBy(lesson.date.desc(), lesson.startTime.desc(), lesson.endTime.desc())
                .fetchFirst());
    }

    @Override
    public Optional<LessonMetaRes> findLessonMetaByLectureIdAfterNow(Long lectureId) {
        return Optional.ofNullable(queryFactory.select(Projections.constructor(LessonMetaRes.class, lesson.lessonId, lesson.lecture.lectureId, lesson.type, lesson.date, Projections.constructor(Schedule.class, lesson.weekday, lesson.startTime, lesson.endTime), lesson.participants))
                .from(lesson)
                .where(lesson.lecture.lectureId.eq(lectureId), lesson.date.after(LocalDate.now()))
                .orderBy(lesson.date.asc(), lesson.startTime.asc(), lesson.endTime.asc())
                .fetchFirst());
    }

    @Override
    public void deleteAllByLectureIdAndDate(Long lectureId, LocalDate startDate, LocalDate endDate) {
        queryFactory.update(lesson)
                .set(lesson.deletedAt, LocalDateTime.now())
                .where(lesson.lecture.lectureId.eq(lectureId), lesson.date.between(startDate, endDate), lesson.type.eq(LessonType.Regular))
                .execute();
    }

    @Override
    public Optional<Long> findRecentLessonIdByLectureId(Long lectureId) {
        return Optional.ofNullable(queryFactory.select(lesson.lessonId)
                .from(lesson)
                .leftJoin(lecture).on(lesson.lecture.lectureId.eq(lecture.lectureId))
                .where(lesson.lecture.lectureId.eq(lectureId), lesson.deletedAt.isNull(), lesson.date.before(LocalDate.now().plusDays(1L)))
                .orderBy(lesson.date.desc(), lesson.startTime.desc(), lesson.endTime.desc())
                .fetchFirst()
        );
    }

    @Override
    public Optional<List<Long>> findRecentLessonIdByLectureIdList(List<Long> lectureIdList) {
        return Optional.ofNullable(
            queryFactory.select(lesson.lessonId)
                    .from(lesson)
                    .where(lesson.lecture.lectureId.in(lectureIdList), lesson.deletedAt.isNull(), lesson.date.before(LocalDate.now().plusDays(1L)))
                    .orderBy(lesson.date.desc(), lesson.startTime.desc(), lesson.endTime.desc())
                    .transform(groupBy(lesson.lecture.lectureId).list(lesson.lessonId))
        );
    }

    @Override
    public List<Long> findAllLessonIdByLectureIdList(List<Long> lectureIdList) {
        return queryFactory.select(lesson.lessonId)
                        .from(lesson)
                        .where(lesson.lecture.lectureId.in(lectureIdList))
                        .fetch();
    }

    @Override
    public void deleteAllByLectureLectureIdList(List<Long> lectureIdList) {
        queryFactory.update(lesson)
                .set(lesson.deletedAt, LocalDateTime.now())
                .where(lesson.lecture.lectureId.in(lectureIdList))
                .execute();
    }

    @Override
    public List<Long> findTodayLessonIdByDate(LocalDate date) {
        return queryFactory.select(lesson.lessonId)
                .from(lesson)
                .leftJoin(lecture).on(lecture.lectureId.eq(lesson.lecture.lectureId))
                .leftJoin(memberAndLecture).on(memberAndLecture.lecture.lectureId.eq(lecture.lectureId))
                .leftJoin(member).on(memberAndLecture.member.memberId.eq(member.memberId))
                .where(lesson.deletedAt.isNull(), lecture.deletedAt.isNull(), memberAndLecture.deletedAt.isNull(), lesson.date.eq(date), member.token.isNotNull(), memberAndLecture.isTeacher.eq(Boolean.TRUE), lesson.deliveredAt.eq(BatchStatus.READY))
                .fetch();
    }

    @Override
    public List<BatchNotificationDto> findAllByDate(List<Long> lessonIdList) {
        return queryFactory.selectFrom(lesson)
                .leftJoin(lecture).on(lecture.lectureId.eq(lesson.lecture.lectureId))
                .leftJoin(memberAndLecture).on(memberAndLecture.lecture.lectureId.eq(lecture.lectureId))
                .leftJoin(member).on(memberAndLecture.member.memberId.eq(member.memberId))
                .where(lesson.lessonId.in(lessonIdList), member.token.isNotNull(), memberAndLecture.isTeacher.eq(Boolean.TRUE))
                .transform(groupBy(memberAndLecture.member.memberId)
                        .list(Projections.constructor(BatchNotificationDto.class, memberAndLecture.member.memberId, member.token,
                                list(Projections.constructor(BatchLessonDto.class, lecture.name, lesson.date, lesson.startTime, lesson.endTime)))));
    }

    @Override
    public void updateLessonProcessingByDate(List<Long> lessonIdList) {
        queryFactory.update(lesson)
                .set(lesson.deliveredAt, BatchStatus.PROCESSING)
                .where(lesson.lessonId.in(lessonIdList))
                .execute();
    }

    @Override
    public void updateLessonSentByDate(List<Long> lessonIdList) {
        queryFactory.update(lesson)
                .set(lesson.deliveredAt, BatchStatus.SENT)
                .where(lesson.lessonId.in(lessonIdList))
                .execute();
    }

    @Override
    public Optional<LocalDate> findLessonDateByLessonId(Long lessonId) {
        return Optional.ofNullable(
                queryFactory.select(lesson.date)
                        .from(lesson)
                        .where(lesson.lessonId.in(lessonId), lesson.deletedAt.isNull())
                        .fetchFirst()
        );
    }
}
