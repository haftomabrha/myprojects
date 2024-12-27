package com.muluneh.MUCSchool.service;

import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
@Service
public class RegisterService {

    @Autowired
    private FirstSemesterRankRepository firstSemesterRankRepository;
    @Autowired
    private SecondSemesterRankRepository secondSemesterRankRepository;
    @Autowired
    private AverageRankRepository averageRankRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private AccountRepository accountRepository;
    private String message="";

    public String registerExistedStudent(Account account) {
        String username=account.getUsername();
        Optional<Account> accountOptional=accountRepository.findByUsername(username);
        if(accountOptional.isEmpty()){
            throw new RuntimeException("Student has no account");
        }
         Account account1=accountOptional.get();
        Optional<Student> studentOptional = studentRepository.findByAccount(account1);

        if (studentOptional.isEmpty()) {
            throw new RuntimeException("Student not found for the given account.");
        }

        Student student = studentOptional.get();
        String gradeLevel = student.getGradeLevel();

        if (isFirstSemesterPassed(student,gradeLevel)) {

        if (isSecondSemesterCompleted(student, gradeLevel)) {
            Optional<AverageRank> averageRankOptional = averageRankRepository.findByStudentAndGradeLevel(student,gradeLevel);
            if(averageRankOptional.isEmpty()){
                throw new RuntimeException("Student mark first semester and second semester is not committed to average rank");
            }
            AverageRank averageRank=averageRankOptional.get();
            double average=averageRank.getAverage();
            if (average>= 50){
                promoteToNextGrade(student, gradeLevel);
            }
        }

        else {
            registerSecondSemester(student, gradeLevel);
        }
        }

        return message;
    }

    private boolean isFirstSemesterPassed(Student student, String gradeLevel) {
        Optional<FirstSemesterRank> firstSemesterRankOptional = firstSemesterRankRepository.findByStudentAndGradeLevel(student,gradeLevel);
              if(firstSemesterRankOptional.isEmpty()){
                  throw new RuntimeException("Student has no finnish first semester");
              }
              FirstSemesterRank firstSemesterRank=firstSemesterRankOptional.get();
              double average=firstSemesterRank.getAverage();
         if(average>=50){
             return true;
         }
         else {
             return false;
         }
    }


    private boolean isSecondSemesterCompleted(Student student, String gradeLevel) {
        Optional<SecondSemesterRank> secondSemesterRankOptional = secondSemesterRankRepository.findByStudentAndGradeLevel(student,gradeLevel);
        if(secondSemesterRankOptional.isEmpty()){
         return false;
        }
        SecondSemesterRank secondSemesterRank=secondSemesterRankOptional.get();
        double average=secondSemesterRank.getAverage();
        if(average>=50){
            return  true;
        }
        else {
            return false;
        }

    }

    private void promoteToNextGrade(Student student, String currentGradeLevel) {

        String nextGradeLevel = getNextGradeLevel(currentGradeLevel);

        if (nextGradeLevel == null) {
            throw new RuntimeException("Promotion not possible for the given grade level.");
        }

        List<Subject> subjects = subjectRepository.findSubjectsByGradeLevel(nextGradeLevel);
        if (subjects.isEmpty()) {
            throw new RuntimeException("No subjects found for grade level: " + nextGradeLevel);
        }
        String newSemester="1st";
        List<Grade> existingGrades = gradeRepository.findByStudentAndGradeLevelAndSemester(student, nextGradeLevel, newSemester);

        int alreadyRegisteredCount = existingGrades.size();
        int allowedRegistrations = 10 - alreadyRegisteredCount;

        // Register new grades if under the limit
        int counter = 0;
        for (Subject subject : subjects) {
            if (!existingGrades.stream().anyMatch(g -> g.getSubject().equals(subject)) && counter < allowedRegistrations) {
                Grade grade = new Grade();
                grade.setStudent(student);
                grade.setSubject(subject);
                grade.setGradeLevel(nextGradeLevel);
                grade.setSemester(newSemester);
                grade.setField(student.getStream());
                grade.setSections(student.getSection());
                gradeRepository.save(grade);
                counter++;
            }
        }

        // Check if total grades exceed 10 after registration
        existingGrades = gradeRepository.findByStudentAndGradeLevelAndSemester(student, nextGradeLevel, newSemester);
        if (existingGrades.size() > 10) {
            // Sort by ID or other criteria to determine which grades to delete
            existingGrades.sort(Comparator.comparing(Grade::getId)); // Oldest first
            List<Grade> excessGrades = existingGrades.subList(10, existingGrades.size());

            // Delete excess grades
            for (Grade grade : excessGrades) {
                gradeRepository.delete(grade);
            }
        }

        if (counter > 0) {
            message = "Registered successfully for " + counter + " subject(s). Excess grades, if any, were removed.";
        } else {
            message = "No new subjects to register; already at the limit. Excess grades, if any, were removed.";
        }
            student.setGradeLevel(nextGradeLevel);
            studentRepository.save(student);
        }

    @Transactional
    private void registerSecondSemester(Student student, String gradeLevel) {
        List<Subject> subjects = subjectRepository.findSubjectsByGradeLevel(gradeLevel);

        if (subjects.isEmpty()) {
            throw new RuntimeException("No subjects found for grade level: " + gradeLevel);
        }

        String newSemester = "2nd";
        List<Grade> existingGrades = gradeRepository.findByStudentAndGradeLevelAndSemester(student, gradeLevel, newSemester);

        int alreadyRegisteredCount = existingGrades.size();
        int allowedRegistrations = 10 - alreadyRegisteredCount;

        // Register new grades if under the limit
        int counter = 0;
        for (Subject subject : subjects) {
            if (!existingGrades.stream().anyMatch(g -> g.getSubject().equals(subject)) && counter < allowedRegistrations) {
                Grade grade = new Grade();
                grade.setStudent(student);
                grade.setSubject(subject);
                grade.setGradeLevel(gradeLevel);
                grade.setSemester(newSemester);
                grade.setField(student.getStream());
                grade.setSections(student.getSection());
                gradeRepository.save(grade);
                counter++;
            }
        }

        // Check if total grades exceed 10 after registration
        existingGrades = gradeRepository.findByStudentAndGradeLevelAndSemester(student, gradeLevel, newSemester);
        if (existingGrades.size() > 10) {
            // Sort by ID or other criteria to determine which grades to delete
            existingGrades.sort(Comparator.comparing(Grade::getId)); // Oldest first
            List<Grade> excessGrades = existingGrades.subList(10, existingGrades.size());

            // Delete excess grades
            for (Grade grade : excessGrades) {
                gradeRepository.delete(grade);
            }
        }

        if (counter > 0) {
            message = "Registered successfully for " + counter + " subject(s). Excess grades, if any, were removed.";
        } else {
            message = "No new subjects to register; already at the limit. Excess grades, if any, were removed.";
        }
    }


    private String getNextGradeLevel(String currentGradeLevel) {
        switch (currentGradeLevel) {
            case "Grade 9":
                return "Grade 10";
            case "Grade 10":
                return "Grade 11";
            case "Grade 11":
                return "Grade 12";
            default:
                return null;
        }
    }
}
