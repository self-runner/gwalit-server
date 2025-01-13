package com.selfrunner.domainmodule.domain.banner.repository;

import com.selfrunner.domainmodule.domain.banner.Banner;
import com.selfrunner.commonmodule.enumerate.banner.BannerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findAllByTypeOrderByPriorityAsc(BannerType type);
}