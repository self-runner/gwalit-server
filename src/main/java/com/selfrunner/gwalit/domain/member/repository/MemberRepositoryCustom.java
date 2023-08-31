package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.MemberType;

import java.util.List;

public interface MemberRepositoryCustom {

    // 회원가입 완료 계정인지 여부 확인
    Boolean existsByPhoneAndType(String phone, MemberType memberType);


    void deleteMemberByMemberIdList(List<Long> memberIdList);
}
