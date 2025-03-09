package com.lawmon.lawmon.dto;

import com.lawmon.lawmon.Entity.Role;
import com.lawmon.lawmon.Entity.ExpertCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String email;
    private String password;
    private String name;
    private Role role;  // 일반 사용자(USER) 또는 전문가(EXPERT)

    // 전문가일 경우 추가 정보 입력
    private String specialty;
    private String licenseNumber;
    private ExpertCategory category;
}