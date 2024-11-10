package com.lawmon.lawmon.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

/**
 *
 * @author : frozzun
 * @filename :Expert.java
 * @since 11/10/24
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expert {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private String name;

  private Specialty specialty;

  private Long rating;

  @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL , orphanRemoval = true)
  private List<ChatExpertSession> chatExpertSessionList;
}
