package com.lawmon.lawmon.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long contractId;

    private String title;

    @Enumerated(EnumType.STRING)
    private Specialty category;

    @Column(name = "pdf_url", columnDefinition = "TEXT")
    private String pdfUrl;
}
