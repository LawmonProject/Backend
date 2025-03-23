package com.lawmon.lawmon.dto.law;

import lombok.Data;

@Data
public class PrecedentDto {
    private String id;
    private String 사건번호;
    private String 데이터출처명;
    private String 사건종류코드;
    private String 사건종류명;
    private String 선고;
    private String 선고일자;
    private String 판례일련번호;
    private String 판결유형;
    private String 법원종류코드;
    private String 법원명;
    private String 판례상세링크;
    private String 사건명;
}