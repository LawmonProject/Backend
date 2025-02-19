package com.lawmon.lawmon.Entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expert extends BaseUser {
    private String name;
    private String specialty;
    private String licenseNumber;
}