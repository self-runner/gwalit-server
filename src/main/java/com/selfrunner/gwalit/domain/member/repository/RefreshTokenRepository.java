package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.RefreshToken;
import com.selfrunner.gwalit.domain.member.enumerate.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteAllByPhoneAndMemberType(String phone, MemberType memberType);

    Optional<RefreshToken> findByPhoneAndMemberType(String phone, MemberType memberType);
}
