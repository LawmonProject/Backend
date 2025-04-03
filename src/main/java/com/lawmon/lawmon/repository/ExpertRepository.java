package com.lawmon.lawmon.repository;

import com.lawmon.lawmon.Entity.Expert;
import com.lawmon.lawmon.Entity.ExpertCategory;
import com.lawmon.lawmon.Entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long> {
    Optional<Expert> findByEmail(String email);
    List<Expert> findByCategory(ExpertCategory category);   // 👈 추가
    List<Expert> findBySpecialty(Specialty specialty);       // 👈 추가
}