package com.selfrunner.gwalit.global.util.gcp;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.selfrunner.gwalit.global.exception.ApplicationException;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class GcsClient {

    @Value("${gcp.bucket}")
    private String bucket;

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.base-url}")
    private String baseUrl;

    public String upload(MultipartFile multipartFile, String dirName) {
        // Validation
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Business Logic
        String now = getDate();
        String ext = multipartFile.getContentType();
        String uuid = UUID.randomUUID().toString();
        String imageUrl = dirName + "/" + now + "_" + uuid + "_" + multipartFile.getOriginalFilename();

        try {
            BlobInfo imageInfo = BlobInfo.newBuilder(bucket, imageUrl)
                    .setContentType(ext)
                    .build();

            storage.createFrom(imageInfo, multipartFile.getInputStream());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.FAILED_UPLOAD_FILE);
        }

        // Response
        return baseUrl + imageUrl;
    }

    public void delete(String imgUrl) {
        // Validation

        // Business Logic
        String fileLocation = imgUrl.substring(baseUrl.length());
        try {
            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
            Blob blob = storage.get(bucket, fileLocation);
            if (blob == null) {
                throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
            }

            Storage.BlobSourceOption precondition =
                    Storage.BlobSourceOption.generationMatch(blob.getGeneration());

            storage.delete(bucket, imgUrl, precondition);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.FAILED_DELETE_FILE);
        }

        // Response
    }

    // 년/월/일로 디렉토리 구분할 때 사용
    private String getDate() {
        LocalDate now = LocalDate.now();

        return now.toString().replace("-", "/");
    }
}
