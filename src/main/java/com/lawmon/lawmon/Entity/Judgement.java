package com.lawmon.lawmon.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author : frozzun
 * @filename :Judgement.java
 * @since 11/10/24
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Judgement {
  @Id
  private Long id;

  @Column(nullable = false)
  private String precedentUrl;

  @Column(nullable = false)
  private Long caseNum;

  @ManyToOne
  @JoinColumn(name = "summaryId", nullable = false)
  private Summary summary;
}
