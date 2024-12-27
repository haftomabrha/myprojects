package com.muluneh.MUCSchool.service;
import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.dto.Role;
import com.muluneh.MUCSchool.dto.TeacherDto;
import com.muluneh.MUCSchool.dto.UserDTO;
import com.muluneh.MUCSchool.repository.*;
import com.muluneh.MUCSchool.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  SectionRepository sectionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private AuthorityRepository authorityRepository; // Add authority repository
    @Autowired
    private CustomPasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetRepository passwordResetRepository;

    // Method to validate age
    private boolean isAgeValid(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birthdate cannot be null");
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return age >= 18;
    }

    // Method to create a new user
    @Transactional
    public UserDTO adminCreateAccounts(UserDTO userDTO) {
        // Validate age
        if (!isAgeValid(userDTO.getBirth_date())) {
            throw new IllegalArgumentException("User must be at least 18 years old to register.");
        }

        // Validate UserDTO
        if (userDTO.getFirstname() == null || userDTO.getLastname() == null || userDTO.getRole() == null||userDTO.getEmail()==null) {
            throw new IllegalArgumentException("Firstname, lastname, and role cannot be null.");
        }

        try {
            String username = generateUniqueUsername();

            // Check if username already exists
            if (accountRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new IllegalArgumentException("This "+userDTO.getEmail()+" email is already used.");
            }

            // Create and save the account
            Account accounts = new Account();
//            PasswordReset passwordReset=new PasswordReset();
            accounts.setEmail(userDTO.getEmail());
            accounts.setUsername(username);
            accounts.setPassword(passwordEncoder.getPasswordEncoder().encode(userDTO.getFirstname() + "@12"));
            accounts.setAccountCreated(LocalDate.now());
            accountRepository.save(accounts); // Save the account
//            passwordReset.setAccount(accounts);
//            passwordReset.setToken(passwordEncoder.getPasswordEncoder().encode(userDTO.getFirstname()+"@12"));
//             passwordReset.setExpiryDate(LocalDate.now().atTime(23,30));
//             passwordResetRepository.save(passwordReset);
            // Check if authority already exists for the account
            if (authorityRepository.existsByAccountAndAuthority(accounts, userDTO.getRole())) {
                throw new RuntimeException("This account already has the specified authority. Please choose a different role.");
            }

            // Create and assign authority based on user role
            Authority authority = new Authority();
            authority.setAccount(accounts);
            authority.setAuthority(userDTO.getRole()); // Assign role from UserDTO
            authorityRepository.save(authority);
            // Create and save the user
                User user = new User();
                user.setFirstname(userDTO.getFirstname());
                user.setLastname(userDTO.getLastname());
                user.setUsername(username);
                user.setBirth_date(userDTO.getBirth_date());
                user.setGender(userDTO.getGender());
                user.setAccount(accounts);

                userRepository.save(user); // Save the user

            // Return the created user as a DTO
            return userDTO; // Modify this if you want to return a specific DTO representation of the created user

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Database error occurred. Please try again.");
        }
    }
    @Transactional
    public TeacherDto teacherAccount(TeacherDto teacherDto){
        // Validate age
        if (!isAgeValid(teacherDto.getBirth_date())) {
            throw new IllegalArgumentException("User must be at least 18 years old to register.");
        }

        // Validate UserDTO
        if (teacherDto.getFirstname() == null || teacherDto.getLastname() ==null ||teacherDto.getEmail()==null) {
            throw new IllegalArgumentException("Firstname, lastname, and email  cannot be null.");
        }

        try {
            String username = generateUniqueUsername();
            if (accountRepository.findByEmail(teacherDto.getEmail()).isPresent()) {
                throw new IllegalArgumentException(
                        "This "+teacherDto.getEmail()+" email is already used.");
            }

            // Create and save the account
            Account accounts = new Account();
            accounts.setEmail(teacherDto.getEmail());
            accounts.setUsername(username);
            accounts.setPassword(passwordEncoder.getPasswordEncoder().encode(teacherDto.getFirstname() + "@12"));
            accounts.setAccountCreated(LocalDate.now());

            // Check if authority already exists for the account
            if (authorityRepository.existsByAccountAndAuthority(accounts, teacherDto.getRole())) {
                throw new RuntimeException("This account already has the specified authority. Please choose a different role.");
            }

            // Create and assign authority based on user role
            Authority authority = new Authority();
            authority.setAccount(accounts);
            authority.setAuthority(String.valueOf(Role.TEACHER)); // Assign role from UserDTO

            Optional<Subject> subjectOptional=subjectRepository.findById(teacherDto.getSubject());
            Teacher user = new Teacher();
            if(subjectOptional.isPresent()) {
                Subject subject = subjectOptional.get();
                accountRepository.save(accounts); // Save the account
                authorityRepository.save(authority);// save authority
                // Create and save the user
                user.setFirstname(teacherDto.getFirstname());
                user.setLastname(teacherDto.getLastname());
                user.setUsername(username);
                user.setBirth_date(teacherDto.getBirth_date());
                user.setGender(teacherDto.getGender());
                user.setAccount(accounts);

                user.setSection(teacherDto.getSection());
                user.setGradeLevel(teacherDto.getGradeLevel());
                user.setDepartment(teacherDto.getDepartment());
                user.setSubject(subject);
                teacherRepository.save(user);

                if(!teacherDto.getSection().isEmpty()) {

                    Section section = new Section();
                    section.setTeacher(user);
                    section.setSection(teacherDto.getSection());
                    section.setGradeLevel(teacherDto.getGradeLevel());
                    sectionRepository.save(section);
                }



            }

             // Save the user

            // Return the created user as a DTO
            return teacherDto; // Modify this if you want to return a specific DTO representation of the created user

        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Database error occurred. Please try again.");
        }
    }
    public TeacherDto editTeacher(TeacherDto teacherDto) {
        // Fetch account and user based on username
        Account account = accountRepository.findByUsername(teacherDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Teacher teacher = teacherRepository.findByUsername(teacherDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Fetch authority linked to the account
        Authority authority = authorityRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Authority not found"));

        Optional<Subject> subjectOptional=subjectRepository.findById(teacherDto.getSubject());

        if(subjectOptional.isPresent()) {
            Subject subject = subjectOptional.get();
            //update email in account table
            account.setPassword(passwordEncoder.getPasswordEncoder().encode(teacherDto.getFirstname() + "@12"));
            account.setEmail(teacherDto.getEmail());
            accountRepository.save(account);

            // Update TeacherTable details
            teacher.setFirstname(teacherDto.getFirstname());
            teacher.setLastname(teacherDto.getLastname());
            teacher.setBirth_date(teacherDto.getBirth_date());
            teacher.setGender(teacherDto.getGender());
            teacher.setAccount(account);
            teacher.setSection(teacherDto.getSection());
            teacher.setGradeLevel(teacherDto.getGradeLevel());
            teacher.setDepartment(teacherDto.getDepartment());
            teacher.setSubject(subject);
            teacherRepository.save(teacher);


            // if teacher has assigned to lead specific section
            if (!teacherDto.getSection().isEmpty()) {
                Optional<Section> sectionOptional = sectionRepository.findByTeacherIdAndSection(teacher.getId(),teacherDto.getSection());
                if(sectionOptional.isPresent()) {
                    Section section=sectionOptional.get();
                    section.setTeacher(teacher);
                    section.setSection(teacherDto.getSection());
                    section.setGradeLevel(teacherDto.getGradeLevel());
                    sectionRepository.save(section);
                }
            }
        }

        // Map updated User to UserDTO and return it
        return  teacherDto;

    }


    // Generate a unique username based on the current year
    private String generateUniqueUsername() {
        String baseUsername = "MU/EMP";
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

    public UserDTO editAccount(UserDTO userDTO) {
        // Fetch account and user based on username
        Account account = accountRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch authority linked to the account
        Authority authority = authorityRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Authority not found"));

        // Update authority role
        authority.setAuthority(userDTO.getRole());
        authorityRepository.save(authority);

        // Update user details
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setGender(userDTO.getGender());
        user.setBirth_date(userDTO.getBirth_date());
        userRepository.save(user);

        // Map updated User to UserDTO and return it
        return  userDTO;

}
    public void deleteAccount(UserDTO userDTO) {
        // Fetch account by username
        Account account = accountRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Fetch user associated with the account
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch authority linked to the account
        Authority authority = authorityRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Authority not found"));

        // Delete authority
        authorityRepository.delete(authority);

        // Delete user
        userRepository.delete(user);

        // Delete account
        accountRepository.delete(account);
    }




    public User getStudentProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with username: " + username));
    }
    public List <Teacher> getAllTeachers(){

    List<Teacher> teachers=teacherRepository.findAll();
    return teachers;
    }
    public List<Student> getAllStudents(){
        List<Student> students=studentRepository.findAll();
        return students;
    }
    public List<Parent> getAllParent(){
        List<Parent> parents=parentRepository.findAll();
        return  parents;
    }
    public List<User> getAllUsers(){
        List<User> users=userRepository.findAll();
        return users;
    }

    public String getUserImage(Account account) {
        return userRepository.findByAccount(account)
                .map(User::getProfileImageUrl) // Retrieve the profile image URL if the user exists
                .orElse("default-profile-image-url"); // Provide a default value if user is not found
    }

}

