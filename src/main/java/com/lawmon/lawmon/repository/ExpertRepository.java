package com.lawmon.lawmon.repository;

import com.lawmon.lawmon.Entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long> {
    Optional<Expert> findByEmail(String email);
}