package com.lawmon.lawmon.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Summary")
@NoArgsConstructor
@Getter
@Setter
public class Summary {

    @Id
    @Column(name = "summary_id", nullable = false, length = 255)
    private String summaryId;

    @Column(name = "message_id", nullable = false)
    private String messageId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
}
