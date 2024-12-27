package com.muluneh.MUCSchool.service;
import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.dto.AddMarkRequest;
import com.muluneh.MUCSchool.dto.GradeDto;
import com.muluneh.MUCSchool.dto.MarkDto;
import com.muluneh.MUCSchool.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeService {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }
    public List<Student> getStudentsBySubject(Integer subjectId) {
        List<Grade> grades = gradeRepository.findBySubjectId(subjectId);

        // Extract unique students from the list of grades
        return grades.stream()
                .map(Grade::getStudent)
                .distinct() // Ensures unique students
                .collect(Collectors.toList());
    }

    // Service method to retrieve grades by student ID and subject ID

    public Grade updateGradesForStudent(Account account, MarkDto markDto,Integer student) {
        Optional<Teacher> teacher = teacherRepository.findByAccount(account);
        if (teacher.isEmpty()) {
            throw new RuntimeException("Teacher not found");

        }
        Optional<Student> student1=studentRepository.findById(student);

        Teacher teacher1 = teacher.get();
        Grade updateMark=new Grade();

       List<Grade> grades=gradeRepository.findByStudent(student1.get());
       for(Grade grade:grades){
           if(grade.getSubject().equals(teacher1.getSubject())){
               updateMark=grade;

           }
       }

        // Set the new marks and total
        updateMark.setTeacher(teacher1);
        updateMark.setActivity1(markDto.getActivity1());
        updateMark.setActivity2(markDto.getActivity2());
        updateMark.setTest1(markDto.getTest1());
        updateMark.setTest2(markDto.getTest2());
        updateMark.setMidExam(markDto.getMidExam());
        updateMark.setFinalExam(markDto.getFinalExam());

        double total = markDto.getActivity1() + markDto.getActivity2() +
                markDto.getFinalExam() + markDto.getMidExam() +
                markDto.getTest1() + markDto.getTest2();
        updateMark.setTotalSum(total);

        return gradeRepository.save(updateMark);
    }
    @Transactional
    public List<Grade> getStudentsBySubjectAndSection(Integer subjectId, String section, Account account) {
        // Retrieve the teacher associated with the provided account
        Optional<Teacher> teacherOptional = teacherRepository.findByAccount(account);

        if (teacherOptional.isPresent()) {
            Teacher teacher = teacherOptional.get();

            // Check if the teacher's subject matches the provided subject ID
            if (teacher.getSubject() != null && subjectId.equals(teacher.getSubject().getId())) {

                // Verify if the teacher is assigned to the specified section
                Optional<Section> sectionOptional = sectionRepository.findByTeacherIdAndSection(teacher.getId(), section);
                if (sectionOptional.isPresent()) {

                    // Fetch and return the list of grades for the specified subject and section
                    List<Grade> grades = gradeRepository.findBySubjectIdAndSections(subjectId, section);
                    if (!grades.isEmpty()) {
                        return grades;
                    } else {
                        System.out.println("No grades found for the specified subject and section.");
                    }
                } else {
                    System.out.println("The teacher is not assigned to the specified section.");
                }
            } else {
                System.out.println("The subject ID does not match the teacher's assigned subject.");
            }
        } else {
            System.out.println("No teacher found with the specified account.");
        }

        // Return an empty list if no grades are found or if the teacher/subject match fails
        return new ArrayList<>();
    }





    @Transactional
    public Grade addMark(AddMarkRequest request, Account account) {
        // Find the teacher associated with the account
        Teacher teacher = teacherRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        // Check if the student exists
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // Check if the subject exists
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        // Create or Update the grade
        Grade grade = gradeRepository.findByStudentIdAndSubjectId(request.getStudentId(), request.getSubjectId())
                .orElse(new Grade()); // If no grade exists, create a new one

        grade.setStudent(student);
        grade.setSubject(subject);
        grade.setActivity1(request.getActivity1());
        grade.setActivity2(request.getActivity2());
        grade.setTest1(request.getTest1());
        grade.setTest2(request.getTest2());
        grade.setMidExam(request.getMidExam());
        grade.setFinalExam(request.getFinalExam());

        // Calculate and set the total sum
        double totalSum =0;
        totalSum=request.getActivity1() + request.getActivity2() +
                request.getTest1() + request.getTest2() +
                request.getMidExam() + request.getFinalExam();
        grade.setTotalSum(totalSum);

        // Set the teacher who added the grade
        grade.setTeacher(teacher);

        // Save the grade (either new or updated)
        return gradeRepository.save(grade);
    }



}

