package com.lawmon.lawmon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class LawSearchService {

    @Value("${law.api.key}")
    private String KEY;

    private final WebClient webClient;

    public CompletableFuture<String> searchLaw(String query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 쿼리 문자열을 인코딩하지 않고 그대로 전달
                String uri = String.format("/DRF/lawSearch.do?OC=%s&target=prec&type=json&query=%s", KEY, query);

                System.out.println("🔵 요청 URL: http://www.law.go.kr" + uri);

                byte[] rawBytes = webClient.get()
                        .uri(uri)
                        .header(HttpHeaders.ACCEPT, "application/json")
                        .header(HttpHeaders.USER_AGENT, "curl/7.79.1")
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .block();

                if (rawBytes == null) {
                    System.out.println("❌ 응답이 null입니다.");
                    return "Error: Empty response";
                }

                String response = new String(rawBytes, java.nio.charset.StandardCharsets.UTF_8);

                System.out.println("🟢 수신된 JSON 응답:\n" + response);
                return response;

            } catch (Exception e) {
                System.out.println("❌ 예외 발생: " + e.getMessage());
                return "Error: " + e.getMessage();
            }
        });
    }
}