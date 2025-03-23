package com.lawmon.lawmon.dto.law;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LawSearchWrapperDto {

    @JsonProperty("PrecSearch")
    private LawSearchDto precSearch;
}