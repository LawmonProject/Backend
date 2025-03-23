package com.lawmon.lawmon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${AWS_S3_BUCKET}")
    private String bucket;

    @Value("${AWS_REGION}")
    private String region;

    public String convertAndUpload(MultipartFile multipartFile, String dirName) throws IOException {
        log.info("[Service] 파일 업로드 시작: 파일명={}, MIME 타입={}",
                multipartFile.getOriginalFilename(), multipartFile.getContentType());

        File uploadFile = convert(multipartFile);
        return uploadToS3(uploadFile, dirName);
    }

    private String uploadToS3(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + uploadFile.getName();
        log.info("[Service] S3 업로드 준비: fileName={}", fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, Paths.get(uploadFile.getAbsolutePath()));
        removeNewFile(uploadFile);

        log.info("[Service] S3 업로드 완료: {}", fileName);

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("[Service] 임시 파일 삭제 완료: {}", targetFile.getName());
        } else {
            log.warn("[Service] 임시 파일 삭제 실패: {}", targetFile.getName());
        }
    }

    private File convert(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("upload_", "_" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }
        log.info("[Service] 파일 변환 성공: {}", tempFile.getAbsolutePath());
        return tempFile;
    }
}
