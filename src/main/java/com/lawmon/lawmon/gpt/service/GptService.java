package com.lawmon.lawmon.gpt.service;

import com.lawmon.lawmon.Entity.ChatMessage;
import com.lawmon.lawmon.gpt.dto.GptRequestDto;
import com.lawmon.lawmon.gpt.dto.GptResponseDto;
import com.lawmon.lawmon.repository.ChatMessageRepo;
import com.lawmon.lawmon.repository.ChatRoomRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

  @Value("${openai.api.key}")
  private String apiKey;

  @Value("${openai.api.model}")
  private String model;

  private static final String API_URL = "https://api.openai.com/v1/chat/completions";
  private static final String SYSTEM_PROMPT_SUMMARY = "너는 대화 요약을 잘하는 AI입니다. 사용자가 주고받은 대화 내용을 간결하고 핵심적으로 요약해주세요.";

  private final ChatRoomRepo chatRoomRepo;
  private final ChatMessageRepo chatMessageRepo;
  private final RestTemplate restTemplate = new RestTemplate();


  public String getChatSummary(String roomId) {
    List<ChatMessage> ChatMessages = chatMessageRepo.findByRoomId(roomId);

    StringBuilder fullConversation = new StringBuilder();
    for(ChatMessage chatMessage : ChatMessages) {
      String message = chatMessage.getSender() + ":" + chatMessage.getMessage();
      fullConversation.append(message).append("\n");
    }
    log.info("gpt 한테 보낼 말 : \n{}", fullConversation.toString());

    // GPT 요청 데이터 생성
    GptRequestDto requestDto = GptRequestDto.builder()
      .model(model)
      .messages(Collections.singletonList(GptRequestDto.Message.systemMessage(SYSTEM_PROMPT_SUMMARY)))
      .build();
    requestDto.getMessages().add(GptRequestDto.Message.userMessage(fullConversation.toString()));

    // 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(apiKey);

    // HTTP 엔티티 생성
    HttpEntity<GptRequestDto> entity = new HttpEntity<>(requestDto, headers);

    ResponseEntity<GptResponseDto> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, GptResponseDto.class);

    return Objects.requireNonNull(response.getBody()).getChoices().getFirst().getMessage().getContent();
  }
}
