package com.lawmon.lawmon.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptContractSummaryDto {

    private Summary summary;
    private List<LegalRisk> legalRisks;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private String contractType;
        private List<String> mainClauses;
        private List<String> specialConditions;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LegalRisk {
        private String issue;
        private String reason;
        private String severity;
    }
}
