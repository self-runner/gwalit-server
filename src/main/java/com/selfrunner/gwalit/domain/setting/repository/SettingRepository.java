package com.selfrunner.gwalit.domain.setting.repository;

import com.selfrunner.gwalit.domain.setting.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long> {
}
