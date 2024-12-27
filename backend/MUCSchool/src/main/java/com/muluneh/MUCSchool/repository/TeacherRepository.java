package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByAccount(Account account);
    Optional<Teacher>findBySubjectId(Integer subjectId);
    Optional<Teacher> findByUsername(String username);
}
