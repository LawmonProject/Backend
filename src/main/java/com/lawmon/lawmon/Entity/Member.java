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
 * @filename :Member.java
 * @since 11/10/24
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String profileImage;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private Specialty specialty;

  @Column(nullable = false)
  private Long rating;

  public enum Specialty {
    USER, EXPERT
  }
}
