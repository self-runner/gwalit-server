package com.selfrunner.domainmodule.domain.member.repository;

import com.selfrunner.domainmodule.domain.member.AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, Long> {

    void deleteAllByPhone(String phone);

    Optional<AuthorizationCode> findByPhone(String phone);
}
