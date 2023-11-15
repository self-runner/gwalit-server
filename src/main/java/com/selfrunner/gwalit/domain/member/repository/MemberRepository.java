package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findAllByPhone(String phone);
}
