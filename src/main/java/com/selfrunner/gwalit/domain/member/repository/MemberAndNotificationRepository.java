package com.selfrunner.gwalit.domain.member.repository;

import com.selfrunner.gwalit.domain.member.entity.MemberAndNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAndNotificationRepository extends JpaRepository<MemberAndNotification, Long> {
}
