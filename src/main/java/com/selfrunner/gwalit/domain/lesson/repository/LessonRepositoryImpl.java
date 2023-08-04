package com.selfrunner.gwalit.domain.lesson.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.lesson.dto.response.LessonMetaRes;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

    @Override
    public Optional<List<LessonMetaRes>> findAllLessonMetaByLectureId(Long lectureId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(lesson)
                        .innerJoin(lecture).on(lesson.lecture.eq(lecture))
                        .innerJoin(memberAndLecture).on(lecture.eq(memberAndLecture.lecture))
                        .innerJoin(member).on(member.eq(memberAndLecture.member))
                        .where(lesson.lecture.lectureId.eq(lectureId))
                        .transform(groupBy(lesson).list(Projections.constructor(LessonMetaRes.class, lesson.lessonId, lesson.date, lesson.time,
                                list(Projections.constructor(MemberMeta.class, member.memberId, member.name, memberAndLecture.isTeacher))))));
    }

}
