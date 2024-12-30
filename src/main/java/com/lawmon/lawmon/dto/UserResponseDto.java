package com.lawmon.lawmon.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private String specialty; // "USER" 또는 "EXPERT"
}
