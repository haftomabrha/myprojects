package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset,Long> {
    Optional<PasswordReset> findByToken(String token);

}
