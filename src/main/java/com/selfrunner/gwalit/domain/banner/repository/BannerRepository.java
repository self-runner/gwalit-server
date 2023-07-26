package com.selfrunner.gwalit.domain.banner.repository;

import com.selfrunner.gwalit.domain.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long>, BannerRepositoryCustom {

}