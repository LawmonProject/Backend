package com.lawmon.lawmon.gpt.controller;

import com.lawmon.lawmon.exception.ErrorResponse;
import com.lawmon.lawmon.gpt.dto.GptContractSummaryDto;
import com.lawmon.lawmon.gpt.service.GptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/gpt")
@Tag(name = "ChatGPT API", description = "ChatGPT API")
public class GptController {

  private final GptService gptService;

  @GetMapping("/summary")
  public ResponseEntity<String> chat(@RequestParam String roomId) {
    try {
      String summary = gptService.getChatSummary(roomId);
      return ResponseEntity.status(HttpStatus.OK).body(summary);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Invalid roomId: " + roomId);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An unexpected error occurred.");
    }
  }

  @Operation(summary = "계약서 리스크 분석", description = "ChatGPT를 통해 계약서의 핵심 정보를 파악 후 리스크를 분석합니다.")
  @PostMapping("/contracts/{contract_id}/summary")
  public ResponseEntity<?> contractSummary(@PathVariable("contract_id") Long contractId) {
    try {
      GptContractSummaryDto summaryDto = gptService.summarizeContractPdf(contractId);
      return ResponseEntity.status(HttpStatus.OK).body(summaryDto);
    } catch (IllegalArgumentException e) {
      log.warn("[Controller] 계약서 조회 실패: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(new ErrorResponse(400, e.getMessage()));
    } catch (IOException e) {
      log.error("[Controller] GPT 응답 파싱 중 IOException 발생", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse(500, "GPT 응답 파싱 중 오류 발생"));
    } catch (Exception e) {
      log.error("[Controller] 서버 내부 오류 발생", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorResponse(500, "서버 내부 오류 발생"));
    }
  }
}
