package com.selfrunner.domainmodule.domain.notification.repository;

import com.selfrunner.domainmodule.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
}
