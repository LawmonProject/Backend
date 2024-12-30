package com.lawmon.lawmon.repository;

import com.lawmon.lawmon.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByEmail(String email);
}
