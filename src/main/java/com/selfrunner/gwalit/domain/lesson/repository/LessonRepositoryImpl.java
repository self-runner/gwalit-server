package com.selfrunner.gwalit.domain.lesson.repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonProgressRes;
import com.selfrunner.gwalit.domain.lesson.entity.LessonType;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.selfrunner.gwalit.domain.lecture.entity.QLecture.lecture;
import static com.selfrunner.gwalit.domain.lesson.entity.QLesson.lesson;

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
                        .transform(groupBy(lesson.lessonId).list(Projections.constructor(LessonMetaRes.class, lesson.lessonId, lesson.lecture.lectureId, lesson.type, lesson.date, lesson.time, lesson.participants)))
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
                        .transform(groupBy(lesson.lessonId).list(Projections.constructor(LessonMetaRes.class, lesson.lessonId, lesson.lecture.lectureId, lesson.type, lesson.date, lesson.time, lesson.participants)))
        );
    }

    @Override
    public Optional<List<LessonProgressRes>> findAllProgressByLectureId(Long lectureId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(lesson)
                        .where(lesson.lecture.lectureId.eq(lectureId), lesson.type.ne(LessonType.Deleted))
                        .transform(groupBy(lesson.lessonId).list(Projections.constructor(LessonProgressRes.class, lesson.lessonId, lesson.lecture.lectureId, lesson.date, lesson.progresses)))
        );
    }
}
