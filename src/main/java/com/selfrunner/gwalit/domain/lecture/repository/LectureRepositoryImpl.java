package com.selfrunner.gwalit.domain.lecture.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMainRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMetaRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.selfrunner.gwalit.domain.lecture.entity.QLecture.lecture;
import static com.selfrunner.gwalit.domain.member.entity.QMember.member;
import static com.selfrunner.gwalit.domain.member.entity.QMemberAndLecture.memberAndLecture;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<Long>> findAllLectureIdByMember(Member m) {
        return Optional.ofNullable(queryFactory.select(memberAndLecture.lecture.lectureId)
                .from(memberAndLecture)
                .where(memberAndLecture.member.eq(m))
                .fetch()
        );
    }

    @Override
    public Optional<List<GetLectureMainRes>> findAllLectureMainByLectureIdList(Member m, List<Long> lectureIdList) {
        return Optional.ofNullable(queryFactory.selectFrom(lecture)
                .leftJoin(memberAndLecture).on(lecture.eq(memberAndLecture.lecture))
                .leftJoin(member).on(member.eq(memberAndLecture.member))
                .where(lecture.lectureId.in(lectureIdList), memberAndLecture.deletedAt.isNull())
                .orderBy(lecture.lectureId.asc(), new CaseBuilder()
                        .when(memberAndLecture.member.memberId.eq(m.getMemberId())).then(0)
                            .otherwise(1).asc())
                .transform(groupBy(lecture.lectureId)
                        .list(Projections.constructor(GetLectureMainRes.class, lecture.lectureId, memberAndLecture.name, memberAndLecture.color, lecture.subject,
                                list(Projections.constructor(MemberMeta.class, member.memberId, member.name, memberAndLecture.isTeacher))))));

    }

    @Override
    public Optional<List<GetLectureMetaRes>> findAllLectureMetaByLectureIdList(Member m, List<Long> lectureIdList) {
        return Optional.ofNullable(queryFactory.selectFrom(lecture)
                .leftJoin(memberAndLecture).on(memberAndLecture.lecture.eq(lecture))
                .leftJoin(member).on(member.eq(memberAndLecture.member))
                .where(lecture.lectureId.in(lectureIdList), memberAndLecture.deletedAt.isNull())
                .orderBy(lecture.lectureId.asc(), new CaseBuilder()
                        .when(memberAndLecture.member.memberId.eq(m.getMemberId())).then(0)
                        .otherwise(1).asc())
                .transform(groupBy(lecture.lectureId)
                        .list(Projections.constructor(GetLectureMetaRes.class, lecture.lectureId, memberAndLecture.name, memberAndLecture.color, lecture.subject, lecture.subjectDetail, lecture.startDate, lecture.endDate, lecture.schedules,
                                list(Projections.constructor(MemberMeta.class, member.memberId, member.name, memberAndLecture.isTeacher))))));
    }

    @Override
    public void deleteAllByLectureIdList(List<Long> lectureIdList) {
        queryFactory.update(lecture)
                .set(lecture.deletedAt, LocalDateTime.now())
                .where(lecture.lectureId.in(lectureIdList))
                .execute();
    }
}
