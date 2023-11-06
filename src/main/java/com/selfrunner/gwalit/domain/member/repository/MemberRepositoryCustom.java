package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberState;
import com.selfrunner.gwalit.domain.member.entity.MemberType;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    // 회원가입 완료 계정인지 여부 확인
    Optional<Member> findActiveByPhoneAndType(String phone, MemberType memberType);

    Optional<Member> findInviteByPhoneAndTypeAndState(String phone, MemberType type);

    void deleteMemberByMemberIdList(List<Long> memberIdList);

    Optional<Member> findNotFakeByPhoneAndType(String phone, MemberType memberType);

    List<String> findTokenList();

    List<String> findTokenListByMemberIdList(List<Long> memberId);
}
