package com.selfrunner.gwalit.domain.banner.service;

import com.selfrunner.gwalit.domain.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;

    public Void register() {
        //bannerRepository.save();
        return null;
    }
}
