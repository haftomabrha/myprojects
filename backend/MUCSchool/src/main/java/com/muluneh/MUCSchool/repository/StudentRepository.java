package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.domain.Student;
import com.muluneh.MUCSchool.domain.Subject;
import com.muluneh.MUCSchool.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByGradeLevelAndSection( String section,String gradeLevel);
    Optional<Student> findByAccount(Account account);
    List<Student> findAllByGradeLevel(String gradeLevel);
    List<Student> findAllBySection(String section);
    Optional<Student> findByEmergency(Integer emergency);

    Optional<Student> findById(Integer id);
}
