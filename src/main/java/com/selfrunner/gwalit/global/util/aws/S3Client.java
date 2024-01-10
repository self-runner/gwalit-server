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

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.baseUrl}")
    private String baseUrl;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // Validation
        if(multipartFile.isEmpty()) {
            throw new ApplicationException(ErrorCode.NO_BANNER_IMAGE);
        }

        // Business Logic
        String now = getDate();
        String uuid = UUID.randomUUID().toString();
        String imageUrl = dirName + "/" + now + "_" + uuid + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        objMeta.setContentLength(multipartFile.getInputStream().available());

        // Check File upload
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, imageUrl, multipartFile.getInputStream(), objMeta)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.FAILED_UPLOAD_FILE);
        }


        // Response
        return baseUrl + imageUrl;
    }

    public void delete(String imageUrl) {
        // Validation
        String fileName = imageUrl.substring(baseUrl.length()); // baseUrl 제거
        if(!amazonS3Client.doesObjectExist(bucket, fileName)) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
        }

        // Business Logic
        try {
            amazonS3Client.deleteObject(bucket, fileName);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.FAILED_DELETE_FILE);
        }
    }

    // 년/월/일로 디렉토리 구분할 때 사용
    public String getDate() {
        LocalDate now = LocalDate.now();

        return now.toString().replace("-", "/");
    }
}
