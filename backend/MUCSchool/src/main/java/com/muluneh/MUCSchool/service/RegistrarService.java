package com.muluneh.MUCSchool.service;

import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.dto.*;
import com.muluneh.MUCSchool.repository.*;
import com.muluneh.MUCSchool.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Tuple;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Service
public class RegistrarService {
    private LocalDateTime createdDate;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private CustomPasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private  SectionRepository sectionRepository;

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private  GradeRepository gradeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    private boolean isAgeValidStudent(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birthdate cannot be null");
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return age >= 13;
    }
    @Transactional
    public Student createStudentAccount(StudentDto studentDto) {
        if (!isAgeValidStudent(studentDto.getBirth_date())) {
            throw new IllegalArgumentException("User must be at least 13 years old to register.");
        }

        // Validate UserDTO
        if (studentDto.getFirstname() == null || studentDto.getLastname() == null||studentDto.getEmail()==null) {
            throw new IllegalArgumentException("Firstname , lastname and email cannot be null.");
        }
        try {
            String username = generateStudentUniqueUsername();
// Check if username already exists
            if (accountRepository.findByEmail(studentDto.getEmail()).isPresent()) {
                throw new IllegalArgumentException(
                        "This "+studentDto.getEmail()+" email is already used.");
            }
            // Create an account

            // Create and save the account
            Account accounts = new Account();
            accounts.setEmail(studentDto.getEmail());
            accounts.setUsername(username);
            accounts.setPassword(passwordEncoder.getPasswordEncoder().encode(studentDto.getFirstname() + "@12"));
            accounts.setAccountCreated(LocalDate.now());
            accountRepository.save(accounts);

            // Check if authority already exists for the account
            if (authorityRepository.existsByAccountAndAuthority(accounts,studentDto.getRole())) {
                throw new RuntimeException("This account already has the specified authority. Please choose a different role.");
            }

            // Create and assign authority based on user role
            Authority authority = new Authority();
            authority.setAccount(accounts);
            authority.setAuthority(Role.STUDENT.name());
            // Assign role from UserDTO
            authorityRepository.save(authority);
            //registering new student.

            Student student=new Student();
            student.setFirstname(studentDto.getFirstname());
            student.setLastname(studentDto.getLastname());
            student.setUsername(username);
            student.setBirth_date(studentDto.getBirth_date());
            student.setGender(studentDto.getGender());
            student.setAccount(accounts);

            student.setSection(studentDto.getSection());
            student.setEmergency(studentDto.getEmergency());
            student.setGradeLevel(studentDto.getGradeLevel());
            student.setAverageMark(studentDto.getAverageMark());
            student.setStream(studentDto.getStream());
            studentRepository.save(student);

            List<Subject> subjects = subjectRepository.findSubjectsByGradeLevel(studentDto.getGradeLevel());
            if (subjects.isEmpty()) {
                throw new RuntimeException("No subjects found for the specified grade level.");
            }

            for (Subject subject : subjects) {

                Grade grade = new Grade();

                grade.setStudent(student);
                grade.setSubject(subject); // Set the Subject entity directly
                grade.setGradeLevel(studentDto.getGradeLevel()); // Optional, for additional verification or UI display
                grade.setSemester(studentDto.getSemester());
                grade.setField(studentDto.getStream());
                grade.setSections(studentDto.getSection());

                gradeRepository.save(grade);


            }

            return student;
        }
     catch (
    DataIntegrityViolationException e) {
        throw new RuntimeException("Database error occurred. Please try again.");
    }
    }

    private boolean isAgeValidParent(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birthdate cannot be null");
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return age >= 13;
    }
    @Transactional
    public Parent createParentAccount(ParentDto parentDto) {
        if (!isAgeValidParent(parentDto.getBirth_date())) {
            throw new IllegalArgumentException("User must be at least 13 years old to register.");
        }

        // Validate UserDTO
        if (parentDto.getFirstname() == null || parentDto.getLastname() == null||parentDto.getEmail()==null) {
            throw new IllegalArgumentException("Firstname ,lastname and email cannot be null.");
        }

        try {
            String username = generateParentUniqueUsername();
// Check if username already exists
            if (accountRepository.findByEmail(parentDto.getEmail()).isPresent()) {
                throw new IllegalArgumentException( "This "+parentDto.getEmail()+" email is already used.");
            }
            // Create an account

            // Create and save the account
            Account accounts = new Account();
            accounts.setEmail(parentDto.getEmail());
            accounts.setUsername(username);
            accounts.setPassword(passwordEncoder.getPasswordEncoder().encode(parentDto.getFirstname() + "@12"));
            accounts.setAccountCreated(LocalDate.now());
            accountRepository.save(accounts);


            // Check if authority already exists for the account
            if (authorityRepository.existsByAccountAndAuthority(accounts,parentDto.getRole())) {
                throw new RuntimeException("This account already has the specified authority. Please choose a different role.");
            }

            // Create and assign authority based on user role
            Authority authority = new Authority();
            authority.setAccount(accounts);
            authority.setAuthority(Role.PARENT.name()); // Assign role from UserDTO
            authorityRepository.save(authority);

            Parent user=new Parent();
            user.setFirstname(parentDto.getFirstname());
            user.setLastname(parentDto.getLastname());
            user.setUsername(username);
            user.setBirth_date(parentDto.getBirth_date());
            user.setGender(parentDto.getGender());
            user.setAccount(accounts);
            user.setPhoneNumber(parentDto.getPhoneNumber());
            parentRepository.save(user);


        return user;
        }
        catch (
                DataIntegrityViolationException e) {
            throw new RuntimeException("Database error occurred. Please try again.");
        }
    }
    @Transactional
    public Parent editParentInfo(ParentDto parentDto) {
        Parent parent=new Parent();
        // Fetch account and user based on username
        Account account = accountRepository.findByUsername(parentDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Optional<Parent> parentOptional = parentRepository.findByAccount(account);

        if (parentOptional.isPresent()) {
            parent = parentOptional.get();
            account.setEmail(parentDto.getEmail());
            account.setPassword(passwordEncoder.getPasswordEncoder().encode(parentDto.getFirstname() + "@12"));
            accountRepository.save(account);
            // Update student details
            parent.setFirstname(parentDto.getFirstname());
            parent.setLastname(parentDto.getLastname());
            parent.setGender(parentDto.getGender());
            parent.setBirth_date(parentDto.getBirth_date());
            parent.setPhoneNumber(parentDto.getPhoneNumber());
            parent=parentRepository.save(parent);
        }
        return parent ;
    }

    private String generateStudentUniqueUsername() {
        String baseUsername = "MU/STU";
        Random random = new Random();
        Set<Integer> uniqueDigits = new HashSet<>();
        while (uniqueDigits.size() < 4) {
            uniqueDigits.add(random.nextInt(10)); // Random digit from 0 to 9
        }
        StringBuilder fourDigitNumber = new StringBuilder();
        for (Integer digit : uniqueDigits) {
            fourDigitNumber.append(digit);
        }
        baseUsername += fourDigitNumber.toString();

        int currentYear = LocalDate.now().getYear();
        baseUsername += "/SC" + (currentYear % 100); // Append the decade

        return baseUsername;
    }

    private String generateParentUniqueUsername() {
        String baseUsername = "MU/PAR";
        Random random = new Random();
        Set<Integer> uniqueDigits = new HashSet<>();
        while (uniqueDigits.size() < 4) {
            uniqueDigits.add(random.nextInt(10)); // Random digit from 0 to 9
        }
        StringBuilder fourDigitNumber = new StringBuilder();
        for (Integer digit : uniqueDigits) {
            fourDigitNumber.append(digit);
        }
        baseUsername += fourDigitNumber.toString();

        int currentYear = LocalDate.now().getYear();
        baseUsername += "/SC" + (currentYear % 100); // Append the decade

        return baseUsername;
    }



@Transactional
    public Subject addCourse(SubjectDto subjectDto) {

        // Check if the subject name is not null
        if (subjectDto.getSubjectName() != null) {
            // Check if a course with the given subject name already exists
           Optional <Subject> courseOptional = subjectRepository.findByGradeLevelAndSubjectName(
                    subjectDto.getSubjectName(),subjectDto.getGradeLevel());

            if (!courseOptional.isPresent()) {
                // Create a new course if it does not exist
                Subject course = Subject.builder()
                        .subjectName(subjectDto.getSubjectName())
                        .gradeLevel(subjectDto.getGradeLevel())
                        .build(); // Don't forget to build the course entity

                // Save the new course
                return subjectRepository.save(course);
            } else {
                // Return the existing course
                return courseOptional.get(); // or handle the existing case as needed
            }
        } else {
            throw new IllegalArgumentException("Subject name must not be null."); // Handle null case
        }
    }

    @Transactional
    public Teacher assign(SectionDto sectionDto) {
        Optional<Account> accountOptional=accountRepository.findByUsername(sectionDto.getUsername());
        Account account=accountOptional.get();
        // Find the teacher by username
        Optional<Teacher> teacherOptional = teacherRepository.findByAccount(account);

        // Check if the teacher exists
        if (teacherOptional.isPresent()) {
            Teacher teacher = teacherOptional.get();
            if (sectionRepository.findByTeacherIdAndSection(teacher.getId(),sectionDto.getSection()).isPresent()) {
                throw new IllegalArgumentException("teacher,"+teacher.getFirstname()+"is already assigned to "+sectionDto.getSection());
            }
            Section section = new Section();
            section.setTeacher(teacher);
            section.setGradeLevel(sectionDto.getGradeLevel());
            section.setSection(sectionDto.getSection());

            // Save the section to the repository
            sectionRepository.save(section);

            // Return the teacher with the new section assigned
            return teacher;
        } else {
            // Throw an exception or handle the case where the teacher is not found
            throw new EntityNotFoundException("Teacher with username " + sectionDto.getUsername() + " not found");
        }
    }
    @Transactional
    public StudentDto editStudentInfo(StudentDto studentDto) {
        // Fetch account and user based on username
        Account account = accountRepository.findByUsername(studentDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Optional<Student> studentOptional = studentRepository.findByAccount(account);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            // Update student details
            student.setFirstname(studentDto.getFirstname());
            student.setLastname(studentDto.getLastname());
            student.setGender(studentDto.getGender());
            student.setBirth_date(studentDto.getBirth_date());
            student.setGradeLevel(studentDto.getGradeLevel());
            student.setAverageMark(studentDto.getAverageMark());
            student.setSection(studentDto.getSection());
            student.setStream(studentDto.getStream());
            student.setEmergency(studentDto.getEmergency());
            studentRepository.save(student);

            // Fetch subjects by grade level
            List<Subject> subjects = subjectRepository.findSubjectsByGradeLevel(studentDto.getGradeLevel());
            if (subjects.isEmpty()) {
                throw new RuntimeException("No subjects found for the specified grade level.");
            }

            //Delete old grades associated with the student
            gradeRepository.deleteByStudent(student);

            // Create new grades for the student
            for (Subject subject : subjects) {
                Grade grade = new Grade();
                grade.setStudent(student);
                grade.setSubject(subject); // Set the Subject entity directly
                grade.setGradeLevel(studentDto.getGradeLevel());
                grade.setSemester(studentDto.getSemester());
                grade.setField(studentDto.getStream());
                grade.setSections(studentDto.getSection());
                gradeRepository.save(grade);

            }
        }

            // Map updated student back to DTO
            //studentDto.setId(student.getId()); // Set the ID if needed
            return studentDto;


    }


//    public void deleteAccount(StudentDto studentDto) {
//        // Fetch account by username
//        Account account = accountRepository.findByUsername(studentDto.getUsername())
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        // Fetch authority linked to the account
//        Authority authority = authorityRepository.findByAccount(account)
//                .orElseThrow(() -> new RuntimeException("Authority not found"));
//
//        // Fetch student linked to the account
//        Optional<Student> studentOptional = studentRepository.findByAccount(account);
//        if (studentOptional.isPresent()) {
//            Student student = studentOptional.get();
//
//            // Delete all grades linked to the student
//            gradeRepository.deleteByStudent(student);
//
//            // Delete section association if present
//            Optional<Section> sectionOptional = sectionRepository.findByStudentId(student.getId());
//            sectionOptional.ifPresent(section -> sectionRepository.delete(section));
//
//            // Delete authority linked to the account
//            authorityRepository.delete(authority);
//
//            // Delete student record
//            studentRepository.delete(student);
//
//            // Delete account record
//            accountRepository.delete(account);
//        } else {
//            throw new RuntimeException("Student not found");
//        }
//    }


}
