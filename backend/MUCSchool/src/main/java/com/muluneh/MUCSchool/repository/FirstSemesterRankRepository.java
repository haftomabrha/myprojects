package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.AverageRank;
import com.muluneh.MUCSchool.domain.FirstSemesterRank;
import com.muluneh.MUCSchool.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FirstSemesterRankRepository extends JpaRepository<FirstSemesterRank,Integer> {
    List<FirstSemesterRank> findBySectionAndGradeLevel(String section, String gradeLevel);

    List<FirstSemesterRank> findByStudent(Student student);
    Optional<FirstSemesterRank>findByStudentAndGradeLevel(Student student,String gradeLevel);
    Optional<FirstSemesterRank> findByStudentAndGradeLevelAndSection(Student student, String gradeLevel, String section);


}
