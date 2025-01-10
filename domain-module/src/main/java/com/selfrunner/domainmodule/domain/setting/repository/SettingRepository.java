package com.selfrunner.domainmodule.domain.setting.repository;

import com.selfrunner.domainmodule.domain.setting.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long> {
}
