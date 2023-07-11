package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    // 회원가입 완료 계정인지 여부 확인
    Boolean existsByPhoneAndType(String phone, MemberType memberType);

    Member findByPhoneAndType(String phone, MemberType memberType);

    List<Member> findAllByPhone(String Phone);
}
