package com.selfrunner.gwalit.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.selfrunner.gwalit.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteMemberByMemberIdList(List<Long> memberIdList) {
        queryFactory.update(member)
                .set(member.deletedAt, LocalDateTime.now())
                .where(member.memberId.in(memberIdList), member.state.eq(MemberState.FAKE))
                .execute();
    }
}
