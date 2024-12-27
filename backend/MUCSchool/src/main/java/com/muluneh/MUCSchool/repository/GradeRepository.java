package com.muluneh.MUCSchool.repository;

import com.muluneh.MUCSchool.domain.Grade;
import com.muluneh.MUCSchool.domain.Student;
import com.muluneh.MUCSchool.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade>findBySubject(Subject subject);
    Optional<Grade>findAllByStudentAndSubject(Student student, Subject subject);
    List<Grade> findByStudent(Student student);
    Optional<Grade> findByStudentIdAndSubjectId(Integer studentId, Integer subjectId);
    List<Grade> findBySubjectId(Integer subjectId);
    List<Grade>findBySemesterAndSectionsAndGradeLevel(String semester,String sections,String gradeLevel);
    List<Grade> findBySubjectIdAndSections(Integer subjectId, String sections);
    Grade findByStudentIdAndSubjectId(Long studentId, Long subjectId);
    List<Grade>findByStudentId(Integer studentId);
    List<Grade>findByStudentAndGradeLevelAndSemester(Student student,String gradeLevel,String semester);

    List<Grade> deleteByStudent(Student student);


    }

