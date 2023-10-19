package com.selfrunner.gwalit.domain.banner.service;

import com.selfrunner.gwalit.domain.banner.dto.request.BannerReq;
import com.selfrunner.gwalit.domain.banner.dto.response.BannerRes;
import com.selfrunner.gwalit.domain.banner.entity.Banner;
import com.selfrunner.gwalit.domain.banner.entity.BannerType;
import com.selfrunner.gwalit.domain.banner.repository.BannerRepository;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.aws.S3Client;
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

        Banner banner = bannerReq.toEntity(imageUrl);
        bannerRepository.save(banner);

        // Response
        BannerRes bannerRes = new BannerRes(banner);
        return bannerRes;
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

        // Response
        BannerRes bannerRes = new BannerRes(banner);
        return bannerRes;
    }

    public List<BannerRes> getAll() {
        // Business Logic
        List<Banner> bannerList = bannerRepository.findAllByTypeOrderByPriorityAsc(BannerType.MAIN);

        // Response
        List<BannerRes> bannerRes = bannerList.stream()
                .map(BannerRes::new)
                .collect(Collectors.toList());

        return bannerRes;
    }

    public List<BannerRes> getContent() {
        // Business Logic
        List<Banner> bannerList = bannerRepository.findAllByTypeOrderByPriorityAsc(BannerType.CONTENT);

        // Response
        List<BannerRes> bannerRes = bannerList.stream()
                .map(BannerRes::new)
                .collect(Collectors.toList());

        return bannerRes;
    }

    @Transactional
    public Void delete(Long bannerId) {
        // Validation
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        if(banner.getDeletedAt() != null) {
            throw new ApplicationException(ErrorCode.ALREADY_DELETE_EXCEPTION);
        }

        // Business Logic
        bannerRepository.delete(banner);
        s3Client.delete(banner.getImageUrl());

        // Response
        return null;
    }
}
