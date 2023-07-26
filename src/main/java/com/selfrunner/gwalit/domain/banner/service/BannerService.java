package com.selfrunner.gwalit.domain.banner.service;

import com.selfrunner.gwalit.domain.banner.dto.request.BannerReq;
import com.selfrunner.gwalit.domain.banner.dto.response.BannerRes;
import com.selfrunner.gwalit.domain.banner.entity.Banner;
import com.selfrunner.gwalit.domain.banner.repository.BannerRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.aws.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    private final S3Client s3Client;

    @Transactional
    public BannerRes register(BannerReq bannerReq, MultipartFile multipartFile) {
        // Validation
        if(multipartFile.isEmpty()) {
            throw new ApplicationException(ErrorCode.NO_BANNER_IMAGE);
        }

        // Business Logic
        String imageUrl;
        try {
            imageUrl = s3Client.upload(multipartFile, "banner");
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }

        Banner banner = bannerReq.toEntity(imageUrl);
        bannerRepository.save(banner);

        // Response
        BannerRes bannerRes = new BannerRes().toDto(banner);
        return bannerRes;
    }
}
