package com.lawmon.lawmon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExpertListDto {
    private Long id;
    private String name;
    private String specialty;
}