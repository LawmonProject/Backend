package com.lawmon.lawmon.dto;

import com.lawmon.lawmon.Entity.ExpertCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpertProfileDto {
    private String name;
    private String specialty;
    private String licenseNumber;
    private ExpertCategory category;
    private String email;
    private String phoneNumber;
    private String experience;
    private String profileImageUrl;
}