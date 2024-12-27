package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.AverageRank;
import com.muluneh.MUCSchool.domain.FirstSemesterRank;
import com.muluneh.MUCSchool.domain.SecondSemesterRank;
import com.muluneh.MUCSchool.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SecondSemesterRankRepository  extends JpaRepository<SecondSemesterRank,Integer> {
    List<SecondSemesterRank> findBySectionAndGradeLevel(String section, String gradeLevel);
    List<SecondSemesterRank> findByStudent(Student student);

    Optional<SecondSemesterRank>findByStudentAndGradeLevel(Student student, String gradeLevel);
    Optional<SecondSemesterRank> findByStudentAndGradeLevelAndSection(Student student, String gradeLevel, String section);


}
