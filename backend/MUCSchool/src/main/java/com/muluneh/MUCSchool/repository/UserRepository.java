package com.muluneh.MUCSchool.repository;


import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByAccount(Account account);
    Optional<User> findByUsername(String username);



}
