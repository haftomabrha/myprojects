package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);

    boolean existsByUsername(String username);
}
