package com.lawmon.lawmon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String email;
    private String name;
    private String profileImage;
    private String password;
    private String specialty; // "USER" 또는 "EXPERT"
}
