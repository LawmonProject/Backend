package com.lawmon.lawmon.gpt.controller;

import com.lawmon.lawmon.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/gpt")
public class GptController {

  private final GptService gptService;

  @GetMapping("/summary")
  public ResponseEntity<String> chat(@RequestParam String roomId) {
    try {
      String summary = gptService.getChatSummary(roomId);
      return ResponseEntity.status(HttpStatus.OK).body(summary);
    } catch (IllegalArgumentException e) {
      // 잘못된 roomId가 전달된 경우 400 Bad Request 반환
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Invalid roomId: " + roomId);
    } catch (Exception e) {
      // 예기치 못한 오류 발생 시 500 Internal Server Error 반환
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An unexpected error occurred.");
    }
  }
}
