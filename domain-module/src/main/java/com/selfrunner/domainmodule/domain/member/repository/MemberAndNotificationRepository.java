package com.selfrunner.domainmodule.domain.member.repository;

import com.selfrunner.domainmodule.domain.member.MemberAndNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAndNotificationRepository extends JpaRepository<MemberAndNotification, Long> {
}
