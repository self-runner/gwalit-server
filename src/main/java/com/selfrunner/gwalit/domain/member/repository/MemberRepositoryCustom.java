package com.selfrunner.gwalit.domain.member.repository;

import java.util.List;

public interface MemberRepositoryCustom {

    void deleteMemberByMemberIdList(List<Long> memberIdList);
}
