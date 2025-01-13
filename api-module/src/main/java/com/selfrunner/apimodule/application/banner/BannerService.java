package com.selfrunner.apimodule.application.banner;

import com.selfrunner.commonmodule.dto.banner.request.BannerReq;
import com.selfrunner.commonmodule.dto.banner.response.BannerRes;
import com.selfrunner.commonmodule.exception.ApplicationException;
import com.selfrunner.commonmodule.exception.ErrorCode;
import com.selfrunner.domainmodule.domain.banner.Banner;
import com.selfrunner.commonmodule.enumerate.banner.BannerType;
import com.selfrunner.domainmodule.domain.banner.repository.BannerRepository;
import com.selfrunner.inframodule.s3.S3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

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

        Banner banner = Banner.builder()
                .type(bannerReq.getType())
                .imageUrl(imageUrl)
                .linkUrl(bannerReq.getLinkUrl())
                .information(bannerReq.getInformation())
                .priority(bannerReq.getPriority())
                .build();
        Banner savedBanner = bannerRepository.save(banner);

        // Response
        return new BannerRes(
                savedBanner.getBannerId(),
                savedBanner.getImageUrl(),
                savedBanner.getLinkUrl(),
                savedBanner.getInformation()
        );
    }

    @Transactional
    public BannerRes update(Long bannerId, BannerReq bannerReq, MultipartFile multipartFile) {
        // Validation
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        // Business Logic
        // 변경하고자 하는 이미지가 들어올 경우, 삭제 후 변경
        if(!multipartFile.isEmpty()) {
            try {
                s3Client.delete(banner.getImageUrl());
                String imageUrl = s3Client.upload(multipartFile, "banner");
                banner.updateImage(imageUrl);
            } catch (Exception e) {
                throw new ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
            }
        }

        banner.update(bannerReq);
        Banner updatedBanner = bannerRepository.save(banner);

        // Response
        return new BannerRes(
                updatedBanner.getBannerId(),
                updatedBanner.getImageUrl(),
                updatedBanner.getLinkUrl(),
                updatedBanner.getInformation()
        );
    }

    public List<BannerRes> getAll() {
        // Business Logic
        List<Banner> bannerList = bannerRepository.findAllByTypeOrderByPriorityAsc(BannerType.MAIN);

        // Response
        return bannerList.stream()
                .map(banner -> new BannerRes(
                        banner.getBannerId(),
                        banner.getImageUrl(),
                        banner.getLinkUrl(),
                        banner.getInformation()
                ))
                .collect(Collectors.toList());
    }

    public List<BannerRes> getContent() {
        // Business Logic
        List<Banner> bannerList = bannerRepository.findAllByTypeOrderByPriorityAsc(BannerType.CONTENT);

        // Response
        return bannerList.stream()
                .map(banner -> new BannerRes(
                        banner.getBannerId(),
                        banner.getImageUrl(),
                        banner.getLinkUrl(),
                        banner.getInformation()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long bannerId) {
        // Validation
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(banner.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_EXCEPTION);
        }

        // Business Logic
        bannerRepository.delete(banner);
        s3Client.delete(banner.getImageUrl());

        // Response
    }
}
