package com.selfrunner.gwalit.domain.lecture.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public Optional<List<GetLectureMetaRes>> findAllLectureMetaByMember(Member m) {
        /*
        TODO: No constructor found for class com.selfrunner.gwalit.domain.member.entity.MemberMeta with parameters: [class java.lang.Long, class java.lang.String] 오류 해결 필요
         */
        return Optional.ofNullable(queryFactory.selectFrom(memberAndLecture)
                .leftJoin(member).on(member.eq(memberAndLecture.member))
                .leftJoin(lecture).on(lecture.eq(memberAndLecture.lecture))
                .where(memberAndLecture.member.eq(m))
                .transform(groupBy(memberAndLecture.lecture.lectureId)
                        .list(Projections.constructor(GetLectureMetaRes.class, memberAndLecture.lecture.lectureId, memberAndLecture.lecture.name, memberAndLecture.lecture.color,
                                list(Projections.constructor(MemberMeta.class, memberAndLecture.member.memberId, memberAndLecture.member.name))))));

    }

}
