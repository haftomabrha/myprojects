package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    boolean existsByAccountAndAuthority(Account accounts, String role);
    Optional<Authority> findByAccount(Account account);
}
