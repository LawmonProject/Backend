package com.lawmon.lawmon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawmon.lawmon.dto.law.LawSearchDto;
import com.lawmon.lawmon.dto.law.LawSearchWrapperDto;
import com.lawmon.lawmon.dto.law.PrecedentDto;
import com.lawmon.lawmon.service.LawSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/law")
@RequiredArgsConstructor
public class LawSearchController {

    private final LawSearchService lawSearchService;
    private final ObjectMapper objectMapper;

    @GetMapping("/search")
    public ResponseEntity<?> searchLaw(@RequestParam String query) {
        try {
            CompletableFuture<String> responseFuture = lawSearchService.searchLaw(query);
            String jsonResponse = responseFuture.join();

            LawSearchWrapperDto parsedDto = objectMapper.readValue(jsonResponse, LawSearchWrapperDto.class);
            LawSearchDto precSearch = parsedDto.getPrecSearch();

            if (precSearch == null || precSearch.getPrec() == null) {
                return ResponseEntity.ok(List.of());
            }

            List<PrecedentDto> top3 = precSearch.getPrec()
                    .stream()
                    .limit(3)
                    .toList();

            return ResponseEntity.ok(top3);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ 판례 검색 중 JSON 파싱 실패: " + e.getMessage());
        }
    }
}