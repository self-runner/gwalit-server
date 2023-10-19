package com.selfrunner.gwalit.domain.banner.repository;

import com.selfrunner.gwalit.domain.banner.entity.Banner;
import com.selfrunner.gwalit.domain.banner.entity.BannerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long>, BannerRepositoryCustom {

    List<Banner> findAllByTypeOrderByPriorityAsc(BannerType type);
}