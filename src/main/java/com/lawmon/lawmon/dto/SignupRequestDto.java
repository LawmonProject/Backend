package com.lawmon.lawmon.dto;

import com.lawmon.lawmon.Entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String email;
    private String password;
    private Role role;  // 일반 사용자(USER) 또는 전문가(EXPERT)

    // 전문가일 경우 추가 정보 입력
    private String name;
    private String specialty;
    private String licenseNumber;
}