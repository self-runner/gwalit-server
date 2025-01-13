package com.selfrunner.domainmodule.domain.member.repository;

import com.selfrunner.domainmodule.domain.member.RefreshToken;
import com.selfrunner.commonmodule.enumerate.member.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteAllByPhoneAndMemberType(String phone, MemberType memberType);

    Optional<RefreshToken> findByPhoneAndMemberType(String phone, MemberType memberType);
}
