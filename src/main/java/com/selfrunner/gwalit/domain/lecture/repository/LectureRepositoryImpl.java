package com.selfrunner.gwalit.domain.lecture.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMainRes;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetLectureMetaRes;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public Optional<List<GetLectureMainRes>> findAllLectureMainByLectureIdList(List<Long> lectureIdList) {
        return Optional.ofNullable(queryFactory.selectFrom(lecture)
                .innerJoin(memberAndLecture).on(lecture.eq(memberAndLecture.lecture))
                .innerJoin(member).on(member.eq(memberAndLecture.member))
                .where(memberAndLecture.lecture.lectureId.in(lectureIdList))
                .transform(groupBy(memberAndLecture.lecture.lectureId)
                        .list(Projections.constructor(GetLectureMainRes.class, lecture.lectureId, lecture.name, lecture.color,
                                list(Projections.constructor(MemberMeta.class, member.memberId, member.name, memberAndLecture.isTeacher))))));

    }

    @Override
    public Optional<List<GetLectureMetaRes>> findAllLectureMetaByLectureIdList(List<Long> lectureIdList) {
        return Optional.ofNullable(queryFactory.selectFrom(lecture)
                .innerJoin(memberAndLecture).on(memberAndLecture.lecture.eq(lecture))
                .innerJoin(member).on(member.eq(memberAndLecture.member))
                .where(memberAndLecture.lecture.lectureId.in(lectureIdList))
                .transform(groupBy(lecture.lectureId)
                        .list(Projections.constructor(GetLectureMetaRes.class, lecture.lectureId, lecture.name, lecture.color, lecture.startDate, lecture.endDate, lecture.schedules,
                                list(Projections.constructor(MemberMeta.class, member.memberId, member.name, memberAndLecture.isTeacher))))));
    }

    @Override
    public GetLectureMetaRes findLectureMetaByLectureId(Long id) {
        return queryFactory.select(Projections.constructor(GetLectureMetaRes.class, lecture.lectureId, lecture.name, lecture.color, lecture.startDate, lecture.endDate, lecture.schedules,
                        list(Projections.constructor(MemberMeta.class, member.memberId, member.name, memberAndLecture.isTeacher))))
                .from(lecture)
                .innerJoin(memberAndLecture).on(memberAndLecture.lecture.lectureId.eq(lecture.lectureId))
                .innerJoin(member).on(member.memberId.eq(memberAndLecture.member.memberId))
                .where(lecture.lectureId.eq(id))
                .fetchOne();
    }

}
