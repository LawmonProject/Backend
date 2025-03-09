package com.lawmon.lawmon.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email; // 이메일

  @Column(nullable = false)
  private String password; // 비밀번호

  @Column(nullable = false)
  private String name; // 이름

  @Column(nullable = true)
  private String profileImage; // 프로필 이미지 URL

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Specialty specialty; // 사용자 유형 (USER 또는 EXPERT)

  public enum Specialty {
    USER, EXPERT
  }
}
