package com.lawmon.lawmon.service;

import com.lawmon.lawmon.Entity.Contract;
import com.lawmon.lawmon.Entity.Specialty;
import com.lawmon.lawmon.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {

    private final S3Service s3Service;
    private final ContractRepository contractRepository;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Transactional
    public Contract uploadContract(MultipartFile file, String title, Specialty category) throws IOException {
        log.info("[Service] 업로드 시작 - 파일명={}, MIME 타입={}, 크기={} bytes",
                file.getOriginalFilename(), file.getContentType(), file.getSize());

        validateFile(file);

        String s3Url = s3Service.convertAndUpload(file, "contracts");

        Contract contract = Contract.builder()
                .title(title)
                .category(category)
                .pdfUrl(s3Url)
                .build();

        return contractRepository.save(contract);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 필요합니다.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("업로드할 파일 크기가 너무 큽니다. (최대 10MB)");
        }

        String contentType = file.getContentType();
        log.info("[Service] 파일 검증 - 원본 MIME 타입: {}", contentType);

        if (!"application/pdf".equalsIgnoreCase(contentType)) {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다. PDF 파일만 업로드할 수 있습니다.");
        }
    }
}
