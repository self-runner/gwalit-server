package com.selfrunner.domainmodule.domain.member.repository;

import com.selfrunner.domainmodule.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findAllByPhone(String Phone);
}
