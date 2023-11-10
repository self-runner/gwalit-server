package com.selfrunner.gwalit.domain.notification.repository;

import com.selfrunner.gwalit.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
}
