package com.lawmon.lawmon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalFileService {

    @Value("${upload.profile-image.path}")
    private String uploadDir;

    public String saveProfileImage(MultipartFile file) throws IOException {
        // 디렉토리 존재 여부 확인 후 없으면 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 저장 경로 구성
        String originalFilename = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, originalFilename);
        Files.write(filePath, file.getBytes());

        // 리턴할 상대 경로
        return "/uploads/profile/" + originalFilename;
    }
}