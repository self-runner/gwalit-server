package com.selfrunner.domainmodule.domain.member.repository;

import com.selfrunner.domainmodule.domain.member.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    Optional<Blacklist> findBlacklistByToken(String token);
}
