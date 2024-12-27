package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.swing.plaf.OptionPaneUI;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("SELECT s.subjectName as subjectName, s.gradeLevel as gradeLevel FROM Subject s WHERE s.gradeLevel = :gradeLevel")
    List<Tuple> findSubjectNamesAndGradeLevelsByGradeLevel(@Param("gradeLevel") String gradeLevel);
    Optional<Subject> findBySubjectName(String subjectName);

    Optional<Subject> findByGradeLevelAndSubjectName(String gradeLevel, String subjectName);

    List<Subject> findSubjectsByGradeLevel(String gradeLevel);
    Optional<Subject> findById(Integer Id);
}

