package com.selfrunner.gwalit.domain.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.lecture.dto.response.GetStudentRes;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.member.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Override
    public void deleteMemberAndLectureByMemberIdList(Long lectureId, List<Long> memberIdList) {
        queryFactory.update(memberAndLecture)
                .set(memberAndLecture.deletedAt, LocalDateTime.now())
                .where(memberAndLecture.lecture.lectureId.eq(lectureId), memberAndLecture.member.memberId.in(memberIdList))
                .execute();
    }

    @Override
    public Optional<List<GetStudentRes>> findStudentByMemberAndLectureId(Member member, Long lectureId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(memberAndLecture)
                        .leftJoin(QMember.member).on(QMember.member.memberId.eq(memberAndLecture.member.memberId))
                        .where(memberAndLecture.lecture.lectureId.eq(lectureId), QMember.member.type.ne(MemberType.TEACHER))
                        .transform(groupBy(QMember.member.memberId).list(Projections.constructor(GetStudentRes.class, QMember.member.memberId, QMember.member.name, QMember.member.type, QMember.member.state, QMember.member.phone, QMember.member.school, QMember.member.grade)))
        );
    }

    @Override
    public void deleteMemberAndLectureByLectureId(Long lectureId) {
        queryFactory.update(memberAndLecture)
                .set(memberAndLecture.deletedAt, LocalDateTime.now())
                .where(memberAndLecture.lecture.lectureId.eq(lectureId))
                .execute();
    }

    @Override
    public void deleteMemberAndLecturesByMember(Member member) {
        queryFactory.update(memberAndLecture)
                .set(memberAndLecture.deletedAt, LocalDateTime.now())
                .where(memberAndLecture.member.memberId.eq(member.getMemberId()))
                .execute();
    }

    @Override
    public Optional<MemberAndLecture> findMemberAndLectureByMemberIdAndLectureId(Long memberId, Long lectureId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(memberAndLecture)
                    .leftJoin(lecture).on(memberAndLecture.lecture.lectureId.eq(lecture.lectureId))
                    .where(memberAndLecture.member.memberId.eq(memberId), lecture.lectureId.eq(lectureId))
                    .fetchOne()
        );
    }

    @Override
    public Optional<Member> findMemberAndLectureIdByMemberPhoneAndLectureId(String phone, Long lectureId) {
        return Optional.ofNullable(
            queryFactory.select(member)
                    .from(memberAndLecture)
                    .leftJoin(member).on(memberAndLecture.member.memberId.eq(member.memberId))
                    .where(member.phone.eq(phone), member.type.eq(MemberType.STUDENT), member.state.ne(MemberState.FAKE), memberAndLecture.lecture.lectureId.eq(lectureId), member.deletedAt.isNull(), memberAndLecture.deletedAt.isNull())
                    .fetchOne()
        );
    }
}
