package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.Entity.Contract;
import com.lawmon.lawmon.Entity.Specialty;
import com.lawmon.lawmon.security.JwtUtil;
import com.lawmon.lawmon.service.ContractService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContractController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private com.lawmon.lawmon.repository.ChatMessageRepo chatMessageRepo;

    @MockBean
    private com.lawmon.lawmon.repository.ChatRoomRepo chatRoomMongoRepo;

    @Test
    @DisplayName("POST /contracts/upload - PDF 파일 업로드 성공")
    void uploadContract_ShouldReturn201_WhenValidPdfUploaded() throws Exception {
        // ✅ 테스트용 PDF 파일 생성
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "Dummy PDF content".getBytes(StandardCharsets.UTF_8)
        );

        // ✅ Mock 결과 설정 (파일 업로드 성공)
        Contract mockContract = new Contract(1L, "테스트 계약서", Specialty.Labor, "https://s3-bucket-url/test.pdf");
        when(contractService.uploadContract(any(MockMultipartFile.class), eq("테스트 계약서"), eq(Specialty.Labor)))
                .thenReturn(mockContract);

        // ✅ API 요청 수행
        mockMvc.perform(multipart("/contracts/upload")
                        .file(mockFile)
                        .param("title", "테스트 계약서")
                        .param("category", "Labor")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated()) // 201 응답 확인
                .andExpect(jsonPath("$.contractId").value(1))
                .andExpect(jsonPath("$.title").value("테스트 계약서"))
                .andExpect(jsonPath("$.category").value("Labor"))
                .andExpect(jsonPath("$.pdfUrl").value("https://s3-bucket-url/test.pdf"));
    }

    @Test
    @DisplayName("POST /contracts/upload - PDF 외 다른 형식의 파일 업로드 시 400 반환")
    void uploadContract_ShouldReturn400_WhenInvalidFileUploaded() throws Exception {
        // ✅ 테스트용 PPTX 파일 생성
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.pptx",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "Invalid file format".getBytes(StandardCharsets.UTF_8)
        );

        // ✅ Mock 결과 설정 (예외 발생)
        when(contractService.uploadContract(any(MockMultipartFile.class), any(), any()))
                .thenThrow(new IllegalArgumentException("지원되지 않는 파일 형식입니다. PDF 파일만 업로드할 수 있습니다."));

        // ✅ API 요청 수행
        mockMvc.perform(multipart("/contracts/upload")
                        .file(mockFile)
                        .param("title", "테스트 계약서")
                        .param("category", "Labor")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest()) // 400 응답 확인
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.message").value("지원되지 않는 파일 형식입니다. PDF 파일만 업로드할 수 있습니다."));
    }

    @Test
    @DisplayName("POST /contracts/upload - 파일 없이 요청 시 400 반환")
    void uploadContract_ShouldReturn400_WhenNoFileProvided() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "", "application/pdf", new byte[0]);

        when(contractService.uploadContract(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("파일이 필요합니다."));

        mockMvc.perform(multipart("/contracts/upload")
                        .file(emptyFile)
                        .param("title", "테스트 계약서")
                        .param("category", "Labor")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.message").value("파일이 필요합니다."));
    }

    @Test
    @DisplayName("POST /contracts/upload - 파일 크기 초과 시 400 반환")
    void uploadContract_ShouldReturn400_WhenFileTooLarge() throws Exception {
        // ✅ MockMultipartFile (실제 요청에서는 큰 파일이므로 여기선 생략)
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "large.pdf",
                "application/pdf",
                new byte[15 * 1024 * 1024] // 15MB 파일 (제한 10MB 초과)
        );

        when(contractService.uploadContract(any(MockMultipartFile.class), any(), any()))
                .thenThrow(new IllegalArgumentException("업로드할 파일 크기가 너무 큽니다. (최대 10MB)"));

        mockMvc.perform(multipart("/contracts/upload")
                        .file(mockFile)
                        .param("title", "테스트 계약서")
                        .param("category", "Labor")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest()) // 400 응답 확인
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.message").value("업로드할 파일 크기가 너무 큽니다. (최대 10MB)"));
    }

    @Test
    @DisplayName("POST /contracts/upload - 서버 오류 시 500 반환")
    void uploadContract_ShouldReturn500_WhenServerErrorOccurs() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "Dummy PDF content".getBytes(StandardCharsets.UTF_8)
        );

        when(contractService.uploadContract(any(MockMultipartFile.class), any(), any()))
                .thenThrow(new RuntimeException("서버 내부 오류 발생"));

        mockMvc.perform(multipart("/contracts/upload")
                        .file(mockFile)
                        .param("title", "테스트 계약서")
                        .param("category", "Labor")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError()) // 500 응답 확인
                .andExpect(jsonPath("$.errorCode").value(500))
                .andExpect(jsonPath("$.message").value("서버 내부 오류 발생"));
    }
}