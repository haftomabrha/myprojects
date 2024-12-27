package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.AverageRank;
import com.muluneh.MUCSchool.domain.FirstSemesterRank;
import com.muluneh.MUCSchool.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AverageRankRepository extends JpaRepository<AverageRank, Integer> {
    List<AverageRank> findBySectionAndGradeLevel(String section, String gradeLevel);
    List<AverageRank>findByStudent(Student student);
    Optional<AverageRank>findByStudentAndGradeLevel(Student student, String gradeLevel);

    Optional<AverageRank> findByStudentAndGradeLevelAndSection(Student student, String gradeLevel, String section);
}
