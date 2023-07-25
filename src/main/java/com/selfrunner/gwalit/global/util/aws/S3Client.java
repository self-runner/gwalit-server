package com.selfrunner.gwalit.global.util.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Client {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket")
    private String bucket;

    @Value("${cloud.aws.baseUrl}")
    private String baseUrl;

    public String upload(MultipartFile multipartFile) throws IOException {
        // Validation
        if(multipartFile.isEmpty()) {
            throw new ApplicationException(ErrorCode.NO_BANNER_IMAGE);
        }

        // Business Logic
        LocalDate now = LocalDate.now();
        String uuid = UUID.randomUUID()+toString();
        String imageUrl = uuid+"_"+multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        objMeta.setContentLength(multipartFile.getInputStream().available());

        amazonS3Client.putObject(new PutObjectRequest(bucket, imageUrl, multipartFile.getInputStream(), objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // Response
        String linkUrl = baseUrl+ imageUrl;

        return linkUrl;
    }
}
