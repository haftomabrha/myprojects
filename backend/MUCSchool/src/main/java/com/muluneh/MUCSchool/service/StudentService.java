package com.muluneh.MUCSchool.service;
import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.repository.GradeRepository;
import com.muluneh.MUCSchool.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Transactional
    public List<Student> getGradesByGradeLevelAndSections(String sections, String  gradeLevel) {
        return studentRepository.findAllByGradeLevelAndSection(sections,gradeLevel);
    }
    public List<Student> getStudentsByGradeLevel(String gradeLevel){
        List<Student> students =  studentRepository.findAllByGradeLevel(gradeLevel);
        System.out.println("students are "+students.size());
        return students;
    }
    @Transactional
    public List<Grade> getStudentResult(Account account) {
        Optional<Student> studentOptional = studentRepository.findByAccount(account);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            List<Grade> grades = gradeRepository.findByStudentId(student.getId());
            return grades; // Return the list of grades
        }

        // Return an empty list if the student is not found
        return new ArrayList<>();
    }

    @Transactional
    public List<Grade> getStudentResultBYGradeLevelAndSemester(Account account,String gradeLevel,String semester) {
        Optional<Student> studentOptional = studentRepository.findByAccount(account);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            List<Grade> grades = gradeRepository.findByStudentAndGradeLevelAndSemester(student,gradeLevel,semester);
            return grades; // Return the list of grades
        }

        // Return an empty list if the student is not found
        return new ArrayList<>();
    }
    public Student getStudentInfo(Account account) {
        return studentRepository.findByAccount(account)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found for the given account"));
    }
    public String getUserImage(Account account) {
        return studentRepository.findByAccount(account)
                .map(Student::getProfileImageUrl) // Retrieve the profile image URL if the user exists
                .orElse("default-profile-image-url"); // Provide a default value if user is not found
    }



}
