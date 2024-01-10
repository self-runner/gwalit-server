package com.selfrunner.gwalit.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.selfrunner.gwalit.domain.member.entity.QMember.member;
import static com.selfrunner.gwalit.domain.member.entity.QMemberAndLecture.memberAndLecture;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findActiveByPhoneAndType(String phone, MemberType memberType) {
        return Optional.ofNullable(
                queryFactory.selectFrom(member)
                        .where(member.phone.eq(phone), member.type.eq(memberType), member.state.eq(MemberState.ACTIVE), member.deletedAt.isNull())
                        .fetchFirst()
        );
    }

    @Override
    public Optional<Member> findInviteByPhoneAndTypeAndState(String phone, MemberType type) {
        return Optional.ofNullable(
                queryFactory.selectFrom(member)
                        .where(member.phone.eq(phone), member.type.eq(type), member.state.eq(MemberState.INVITE), member.deletedAt.isNull())
                        .fetchFirst()
        );
    }

    @Override
    public void deleteMemberByMemberIdList(List<Long> memberIdList) {
        queryFactory.update(member)
                .set(member.deletedAt, LocalDateTime.now())
                .where(member.memberId.in(memberIdList), member.type.eq(MemberType.STUDENT), member.state.eq(MemberState.FAKE))
                .execute();
    }

    @Override
    public Optional<Member> findNotFakeByPhoneAndType(String phone, MemberType memberType) {
        return Optional.ofNullable(
                queryFactory.selectFrom(member)
                        .where(member.phone.eq(phone), member.type.eq(memberType), member.state.ne(MemberState.FAKE), member.deletedAt.isNull())
                        .fetchFirst()
        );
    }

    @Override
    public List<String> findTokenList() {
        return queryFactory.select(member.token)
                .from(member)
                .where(member.deletedAt.isNull(), member.token.isNotNull())
                .fetch();
    }

    @Override
    public List<String> findTokenListByMemberIdList(List<Long> memberId) {
        return queryFactory.select(member.token)
                .from(member)
                .where(member.memberId.in(memberId), member.token.isNotNull())
                .fetch();
    }

    @Override
    public List<Member> findMemberListByLectureId(Long lectureId) {
        return  queryFactory.selectFrom(member)
                .leftJoin(memberAndLecture).on(memberAndLecture.member.memberId.eq(member.memberId))
                .where(memberAndLecture.lecture.lectureId.eq(lectureId))
                .fetch();
    }
}
