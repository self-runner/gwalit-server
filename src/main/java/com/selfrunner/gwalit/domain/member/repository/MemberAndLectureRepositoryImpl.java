package com.selfrunner.gwalit.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.selfrunner.gwalit.domain.lecture.entity.QLecture.lecture;
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
}
