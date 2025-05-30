package com.lawmon.lawmon.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expert extends BaseUser {
    private String name;
    private String specialty;
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private ExpertCategory category;

    private String phoneNumber;
    private String experience;
    private String profileImageUrl;
}