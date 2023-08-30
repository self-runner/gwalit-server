package com.selfrunner.gwalit.domain.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberMeta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.selfrunner.gwalit.domain.lecture.entity.QLecture.lecture;
import static com.selfrunner.gwalit.domain.member.entity.QMember.member;
import static com.selfrunner.gwalit.domain.member.entity.QMemberAndLecture.memberAndLecture;

@Repository
@RequiredArgsConstructor
public class MemberAndLectureRepositoryImpl implements MemberAndLectureRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<Long>> findLectureIdByMember(Member member) {
        return Optional.ofNullable(
                queryFactory.select(lecture.lectureId)
                        .from(memberAndLecture)
                        .where(memberAndLecture.member.eq(member))
                        .fetch()
        );
    }

    @Override
    public Optional<List<MemberMeta>> findMemberMetaByLectureLectureId(Long lectureId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(memberAndLecture)
                        .innerJoin(member).on(member.memberId.eq(memberAndLecture.member.memberId))
                        .where(memberAndLecture.lecture.lectureId.eq(lectureId))
                        .transform(groupBy(member.memberId).list(Projections.constructor(MemberMeta.class, member.memberId, member.name, memberAndLecture.isTeacher)
                        ))
        );
    }

    @Override
    public Long findCountByMember(Member member) {
        return queryFactory.select(memberAndLecture.count())
                .from(memberAndLecture)
                .where(memberAndLecture.member.eq(member))
                .fetchFirst();
    }
}
