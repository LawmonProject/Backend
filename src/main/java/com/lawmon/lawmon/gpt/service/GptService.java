package com.lawmon.lawmon.gpt.service;

import com.lawmon.lawmon.Entity.ChatMessage;
import com.lawmon.lawmon.Entity.Contract;
import com.lawmon.lawmon.gpt.dto.GptRequestDto;
import com.lawmon.lawmon.gpt.dto.GptResponseDto;
import com.lawmon.lawmon.gpt.dto.GptContractSummaryDto;
import com.lawmon.lawmon.repository.ChatMessageRepo;
import com.lawmon.lawmon.repository.ChatRoomRepo;
import com.lawmon.lawmon.repository.ContractRepository;
import com.lawmon.lawmon.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  private final ContractRepository contractRepository;
  private final S3Service s3Service;
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

  public GptContractSummaryDto summarizeContractPdf(File pdfFile, String prompt) throws IOException {
    HttpHeaders uploadHeaders = new HttpHeaders();
    uploadHeaders.setBearerAuth(apiKey);
    uploadHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, Object> uploadBody = new LinkedMultiValueMap<>();
    uploadBody.add("file", new FileSystemResource(pdfFile));
    uploadBody.add("purpose", "user_data");

    HttpEntity<MultiValueMap<String, Object>> uploadEntity = new HttpEntity<>(uploadBody, uploadHeaders);
    ResponseEntity<Map> uploadResponse = restTemplate.postForEntity("https://api.openai.com/v1/files", uploadEntity, Map.class);
    String fileId = (String) Objects.requireNonNull(uploadResponse.getBody()).get("id");

    HttpHeaders chatHeaders = new HttpHeaders();
    chatHeaders.setContentType(MediaType.APPLICATION_JSON);
    chatHeaders.setBearerAuth(apiKey);

    Map<String, Object> fileMap = new HashMap<>();
    fileMap.put("type", "file");
    fileMap.put("file", Map.of("file_id", fileId));

    Map<String, Object> promptMap = new HashMap<>();
    promptMap.put("type", "text");
    promptMap.put("text", prompt);

    Map<String, Object> userMessage = new HashMap<>();
    userMessage.put("role", "user");
    userMessage.put("content", List.of(fileMap, promptMap));

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", model);
    requestBody.put("messages", List.of(userMessage));

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, chatHeaders);
    ResponseEntity<Map> gptResponse = restTemplate.postForEntity(API_URL, requestEntity, Map.class);

    if (gptResponse.getBody() == null) {
      log.error("Received null response from GPT");
      throw new IOException("GPT response was null");
    }

    log.info("Raw GPT response: {}", gptResponse.getBody());

    List<Map<String, Object>> choices = (List<Map<String, Object>>) gptResponse.getBody().get("choices");

    if (choices.isEmpty()) {
      log.error("No choices found in GPT response");
      throw new IOException("No choices found in GPT response");
    }

    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
    String content = (String) message.get("content");

    Pattern pattern = Pattern.compile("```json\\s*(\\{.*?})\\s*```", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(content);

    String jsonContent;
    if (matcher.find()) {
        jsonContent = matcher.group(1);
    } else if (content.trim().startsWith("{")) {
        jsonContent = content.trim();
    } else {
        log.error("GPT 응답이 JSON 형식이 아님: {}", content);
        throw new IOException("GPT 응답이 JSON 형식이 아닙니다.");
    }

    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(jsonContent, GptContractSummaryDto.class);
  }

  public GptContractSummaryDto summarizeContractPdf(Long contractId) throws IOException {
    Contract contract = contractRepository.findById(contractId)
            .orElseThrow(() -> new IllegalArgumentException("계약서를 찾을 수 없습니다."));

    String prompt = switch (contract.getCategory()) {
      case Labor -> """
        너는 법률 전문가이며, 근로계약서를 분석하여 아래 형식에 따라 핵심 내용을 요약하고, 법적 리스크를 식별해줘.

        ---
        ## 근로계약서 핵심 요약
        - 근로자 정보:
        - 근무조건:
        - 계약기간:
        - 특이사항:

        ## 법적 리스크 분석
        1. ...
        ---

        어투는 법률 자문서처럼 객관적이고 단정적으로 작성해줘.
        결과는 다음 JSON 스키마에 맞춰 작성해줘. {"summary": {"contractType": "", "mainClauses": [""], "specialConditions": [""]}, "legalRisks": [{"issue": "", "reason": "", "severity": ""}]}
        JSON 외의 설명이나 텍스트는 포함하지 마. 반드시 JSON으로만 응답해.
        """;

      case RealEstate -> """
        너는 법률 전문가이며, 부동산 매매/임대 계약서를 분석하여 아래 형식에 따라 핵심 내용을 요약하고, 법적 리스크를 식별해줘.

        ---
        ## 부동산계약서 핵심 요약
        - 계약 유형: (매매/임대)
        - 부동산 정보:
        - 계약 조건:
        - 보증금/임대료/매매가:
        - 계약기간:
        - 특약 사항:

        ## 법적 리스크 분석
        1. ...
        ---

        어투는 법률 자문서처럼 객관적이고 단정적으로 작성해줘.
        결과는 다음 JSON 스키마에 맞춰 작성해줘. {"summary": {"contractType": "", "mainClauses": [""], "specialConditions": [""]}, "legalRisks": [{"issue": "", "reason": "", "severity": ""}]}
        JSON 외의 설명이나 텍스트는 포함하지 마. 반드시 JSON으로만 응답해.
        """;

      case Insurance -> """
        너는 법률 전문가이며, 보험 계약서를 분석하여 아래 형식에 따라 핵심 내용을 요약하고, 법적 리스크를 식별해줘.

        ---
        ## 보험계약서 핵심 요약
        - 보험 종류 및 대상:
        - 피보험자 정보:
        - 보장 내용 및 제외 사항:
        - 보험료 및 납입 조건:
        - 계약 기간 및 갱신 조건:

        ## 법적 리스크 분석
        1. ...
        ---

        어투는 법률 자문서처럼 객관적이고 단정적으로 작성해줘.
        결과는 다음 JSON 스키마에 맞춰 작성해줘. {"summary": {"contractType": "", "mainClauses": [""], "specialConditions": [""]}, "legalRisks": [{"issue": "", "reason": "", "severity": ""}]}
        JSON 외의 설명이나 텍스트는 포함하지 마. 반드시 JSON으로만 응답해.
        """;

      default -> """
        너는 법률 전문가이며, 계약서를 분석하여 핵심 내용을 요약하고, 법적 리스크를 식별해줘.

        ---
        ## 계약서 핵심 요약
        - 주요 당사자:
        - 핵심 조항:
        - 계약 조건:

        ## 법적 리스크 분석
        1. ...
        ---

        어투는 법률 자문서처럼 객관적이고 단정적으로 작성해줘.
        결과는 다음 JSON 스키마에 맞춰 작성해줘. {"summary": {"contractType": "", "mainClauses": [""], "specialConditions": [""]}, "legalRisks": [{"issue": "", "reason": "", "severity": ""}]}
        JSON 외의 설명이나 텍스트는 포함하지 마. 반드시 JSON으로만 응답해.
        """;
    };

    String url = contract.getPdfUrl();
    InputStream inputStream = s3Service.downloadFile(url);
///
    File tempFile = File.createTempFile("contract_", ".pdf");
    try (FileOutputStream out = new FileOutputStream(tempFile)) {
        inputStream.transferTo(out);
    } catch (software.amazon.awssdk.services.s3.model.NoSuchKeyException e) {
        log.error("[Service] S3에서 파일을 찾을 수 없습니다. url={}", contract.getPdfUrl(), e);
        throw new IOException("[Service] 계약서 파일을 찾을 수 없습니다.");
    }

    return summarizeContractPdf(tempFile, prompt);
  }
}
