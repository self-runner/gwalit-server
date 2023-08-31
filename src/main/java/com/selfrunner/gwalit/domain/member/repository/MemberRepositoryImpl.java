package com.selfrunner.gwalit.domain.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
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
    public Boolean existsByPhoneAndType(String phone, MemberType memberType) {
//        queryFactory.selectFrom(member)
//                .where()
//                .asBop
        return Boolean.FALSE;
    }

    @Override
    public void deleteMemberByMemberIdList(List<Long> memberIdList) {
        queryFactory.update(member)
                .set(member.deletedAt, LocalDateTime.now())
                .where(member.memberId.in(memberIdList), member.state.eq(MemberState.FAKE))
                .execute();
    }
}
