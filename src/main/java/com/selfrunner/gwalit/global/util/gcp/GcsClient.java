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

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class GcsClient {

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucket;

    @Value("${spring.cloud.gcp.storage.base-url}")
    private String baseUrl;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // Validation
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Business Logic
        String now = getDate();
        String originalName = multipartFile.getOriginalFilename();
        String ext = multipartFile.getContentType();
        String uuid = UUID.randomUUID().toString();
        String imagUrl = dirName + "/" + now + "_" + uuid + "_" + multipartFile.getOriginalFilename();

        // BUCKET_NAME = GCS에 등록된 버킷 이름
        // 파일은 https://storage.googleapis.com/{버킷_이름}/{UUID}를 통해 조회할 수 있음
        BlobInfo imageInfo = BlobInfo.newBuilder(bucket, uuid)
                .setContentType(ext)
                .build();

        BlobInfo blobInfo = null;
        try {
            blobInfo = storage.create(
                    imageInfo, multipartFile.getInputStream()
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Response
        return null;
    }

    public void delete(String imgUrl) {
        // Validation

        // Business Logic
        try {
            String projectId = "프로젝트_ID";

        String bucketName = "Bucket_이름";
        String objectName = "삭제할 파일 이름";

        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Blob blob = storage.get(bucketName, objectName);
        if (blob == null) {
            throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION);
        }

        Storage.BlobSourceOption precondition =
                Storage.BlobSourceOption.generationMatch(blob.getGeneration());

        storage.delete(bucketName, objectName, precondition);
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
