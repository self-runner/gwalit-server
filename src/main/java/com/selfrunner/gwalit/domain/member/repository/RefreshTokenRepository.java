package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.RefreshToken;
import com.selfrunner.gwalit.domain.member.enumerate.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteAllByPhoneAndMemberType(String phone, MemberType memberType);
}
