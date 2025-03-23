package com.lawmon.lawmon.dto.law;

import lombok.Data;

import java.util.List;

@Data
public class LawSearchDto {
    private String 키워드;
    private String page;
    private String target;
    private List<PrecedentDto> prec;
    private String totalCnt;
    private String section;
}