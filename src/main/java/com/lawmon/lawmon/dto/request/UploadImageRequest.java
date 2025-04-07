package com.lawmon.lawmon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadImageRequest {

    @Schema(description = "전문가 이메일", example = "expert@test.com")
    private String email;

    @Schema(description = "업로드할 프로필 이미지", type = "string", format = "binary")
    private MultipartFile file;
}