package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.Entity.Contract;
import com.lawmon.lawmon.Entity.Specialty;
import com.lawmon.lawmon.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contracts")
@Tag(name = "Contract API", description = "계약서 API")
public class ContractController {

    private final ContractService contractService;

    @Operation(summary = "계약서 PDF 업로드", description = "PDF 파일을 업로드하여 S3에 저장하고, 저장된 URL을 반환합니다.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadContract(
            @RequestPart("file") @Schema(type = "string", format = "binary") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("category") Specialty category) throws IOException {

        log.info("[Controller] 파일 업로드 요청 - title={}, category={}, filename={}",
                title, category, (file != null ? file.getOriginalFilename() : "파일 없음"));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 필요합니다.");
        }

        Contract contract = contractService.uploadContract(file, title, category);
        log.info("[Controller] 파일 업로드 완료 - 계약서 ID={}", contract.getContractId());

        return ResponseEntity.status(201).body(contract);
    }
}
