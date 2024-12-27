package com.muluneh.MUCSchool.service;

import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RankService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private FirstSemesterRankRepository firstSemesterRankRepository;

    @Autowired
    private SecondSemesterRankRepository secondSemesterRankRepository;

    @Autowired
    private AverageRankRepository averageRankRepository;

    public List<?> setRank(Account account, String semester) {
        // Validate the account
        Optional<Account> accountOptional = accountRepository.findByUsername(account.getUsername());
        if (!accountOptional.isPresent()) {
            return new ArrayList<>();
        }

        Account foundAccount = accountOptional.get();

        // Validate teacher
        Optional<Teacher> teacherOptional = teacherRepository.findByAccount(foundAccount);
        if (!teacherOptional.isPresent()) {
            return new ArrayList<>();
        }

        Teacher teacher = teacherOptional.get();

        // Fetch grades for the given semester, section, and grade level
        List<Grade> grades = gradeRepository.findBySemesterAndSectionsAndGradeLevel(
                semester, teacher.getSection(), teacher.getGradeLevel());

        // Fetch existing ranks for students in the current semester
        Set<Student> studentsWithRanks = new HashSet<>();
        if ("1st".equalsIgnoreCase(semester)) {
            List<FirstSemesterRank> existingRanks = firstSemesterRankRepository.findBySectionAndGradeLevel(
                    teacher.getSection(), teacher.getGradeLevel());
            studentsWithRanks.addAll(existingRanks.stream()
                    .map(FirstSemesterRank::getStudent)
                    .collect(Collectors.toSet()));
        } else if ("2nd".equalsIgnoreCase(semester)) {
            List<SecondSemesterRank> existingRanks = secondSemesterRankRepository.findBySectionAndGradeLevel(
                    teacher.getSection(), teacher.getGradeLevel());
            studentsWithRanks.addAll(existingRanks.stream()
                    .map(SecondSemesterRank::getStudent)
                    .collect(Collectors.toSet()));
        }

        // Map students to their grades, excluding those already ranked
        Map<Student, List<Grade>> studentGradesMap = grades.stream()
                .filter(grade -> !studentsWithRanks.contains(grade.getStudent()))
                .collect(Collectors.groupingBy(Grade::getStudent));

        if (!studentGradesMap.isEmpty()) {
            // Process ranks for students not already ranked
            for (Map.Entry<Student, List<Grade>> entry : studentGradesMap.entrySet()) {
                Student student = entry.getKey();
                List<Grade> studentGrades = entry.getValue();

                // Calculate total sum and average
                double totalSum = studentGrades.stream().mapToDouble(Grade::getTotalSum).sum();
                double average = totalSum / studentGrades.size();

                if ("1st".equalsIgnoreCase(semester)) {
                    // Save first semester rank
                    FirstSemesterRank firstSemesterRank = new FirstSemesterRank();
                    firstSemesterRank.setStudent(student);
                    firstSemesterRank.setFirstSemester(semester);
                    firstSemesterRank.setSection(student.getSection());
                    firstSemesterRank.setGradeLevel(student.getGradeLevel());
                    firstSemesterRank.setTotalSum(totalSum);
                    firstSemesterRank.setAverage(average);
                    firstSemesterRankRepository.save(firstSemesterRank);
                } else if ("2nd".equalsIgnoreCase(semester)) {
                    // Save second semester rank
                    SecondSemesterRank secondSemesterRank = new SecondSemesterRank();
                    secondSemesterRank.setStudent(student);
                    secondSemesterRank.setSection(student.getSection());
                    secondSemesterRank.setGradeLevel(student.getGradeLevel());
                    secondSemesterRank.setSecondSemester(semester);
                    secondSemesterRank.setTotalSum(totalSum);
                    secondSemesterRank.setAverage(average);
                    secondSemesterRankRepository.save(secondSemesterRank);

                    // Calculate and save average rank if the first semester data exists
                    // Calculate and save average rank if the first semester data exists
                    Optional<FirstSemesterRank> firstSemesterRankOptional =
                            firstSemesterRankRepository.findByStudentAndGradeLevel(student,student.getGradeLevel());
                    if (firstSemesterRankOptional.isPresent()) {
                        FirstSemesterRank firstSemesterRank = firstSemesterRankOptional.get();
                        // check whether the student is existed already;
                        Optional<AverageRank> averageRankOptional=averageRankRepository.findByStudentAndGradeLevel(student,student.getGradeLevel());
                        if(averageRankOptional.isEmpty()) {
                            AverageRank averageRank = new AverageRank();
                            averageRank.setStudent(student);
                            averageRank.setGradeLevel(student.getGradeLevel());
                            averageRank.setSection(student.getSection());
                            averageRank.setTotalSum(firstSemesterRank.getTotalSum() + totalSum);
                            averageRank.setAverage((firstSemesterRank.getAverage() + average) / 2);
                            averageRankRepository.save(averageRank);
                        }
                    }

                }
            }

            // Update ranks based on the current semester
            updateRanks(semester, teacher.getSection(), teacher.getGradeLevel());
        }

        // Fetch and return all ranks for the semester
        if ("1st".equalsIgnoreCase(semester)) {
            return firstSemesterRankRepository.findBySectionAndGradeLevel(teacher.getSection(), teacher.getGradeLevel());
        } else if ("2nd".equalsIgnoreCase(semester)) {
            return secondSemesterRankRepository.findBySectionAndGradeLevel(teacher.getSection(), teacher.getGradeLevel());
        } else {
            return new ArrayList<>(); // Default return for unexpected semester values
        }
    }



    private void updateRanks(String semester, String section, String gradeLevel) {
        if ("1st".equalsIgnoreCase(semester)) {
            // Update ranks for first semester
            List<FirstSemesterRank> ranks = firstSemesterRankRepository.findBySectionAndGradeLevel(section, gradeLevel);
            ranks.sort(Comparator.comparingDouble(FirstSemesterRank::getTotalSum).reversed());
            for (int i = 0; i < ranks.size(); i++) {
                ranks.get(i).setRank(i + 1);
                firstSemesterRankRepository.save(ranks.get(i));
            }
        } else if ("2nd".equalsIgnoreCase(semester)) {
            // Update ranks for second semester
            List<SecondSemesterRank> ranks = secondSemesterRankRepository.findBySectionAndGradeLevel(section, gradeLevel);
            ranks.sort(Comparator.comparingDouble(SecondSemesterRank::getTotalSum).reversed());
            for (int i = 0; i < ranks.size(); i++) {
                ranks.get(i).setRank(i + 1);
                secondSemesterRankRepository.save(ranks.get(i));
            }

            // Update ranks for the average
            List<AverageRank> averageRanks = averageRankRepository.findBySectionAndGradeLevel(section, gradeLevel);
            averageRanks.sort(Comparator.comparingDouble(AverageRank::getAverage).reversed());
            for (int i = 0; i < averageRanks.size(); i++) {
                averageRanks.get(i).setRank(i + 1);
                averageRankRepository.save(averageRanks.get(i));
            }
        }
    }
    public List<?> getStudentRank(Account account) {
        // Find the student associated with the account
        Optional<Student> studentOptional = studentRepository.findByAccount(account);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            // Fetch ranks for the student from all rank tables
            List<FirstSemesterRank> firstSemesterRanks = firstSemesterRankRepository.findByStudent(student);
            List<SecondSemesterRank> secondSemesterRanks = secondSemesterRankRepository.findByStudent(student);
            List<AverageRank> averageRanks = averageRankRepository.findByStudent(student);

            // Combine all ranks into a single list to return
            List<Object> studentRanks = new ArrayList<>();
            studentRanks.addAll(firstSemesterRanks);
            studentRanks.addAll(secondSemesterRanks);
            studentRanks.addAll(averageRanks);

            return studentRanks;
        }

        // If no student is found, return an empty list
        return new ArrayList<>();
    }

}
