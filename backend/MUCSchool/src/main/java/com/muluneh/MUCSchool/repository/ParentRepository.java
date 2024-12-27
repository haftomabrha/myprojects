package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.domain.Parent;
import com.muluneh.MUCSchool.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository  extends JpaRepository<Parent,Integer> {
    Optional<Parent> findByAccount(Account account);
    Optional<Parent> findByPhoneNumber(Integer phoneNumber);
}
