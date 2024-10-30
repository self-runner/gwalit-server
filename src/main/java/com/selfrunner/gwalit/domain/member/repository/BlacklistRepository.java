package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    Optional<Blacklist> findBlacklistByToken(String token);
}
