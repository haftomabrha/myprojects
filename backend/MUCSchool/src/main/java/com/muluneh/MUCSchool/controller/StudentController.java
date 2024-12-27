package com.muluneh.MUCSchool.controller;

import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.repository.GradeRepository;
import com.muluneh.MUCSchool.repository.StudentRepository;
import com.muluneh.MUCSchool.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/students")

public class StudentController {

    @Autowired
 private StudentService studentService;
    @GetMapping("/viewMark")
    public ResponseEntity<List<Grade>> getStudentGrades(
            @RequestParam String gradeLevel,
            @RequestParam String semester,
            @AuthenticationPrincipal Account account) {
        // Call the service method to get the student's grades
        List<Grade> grades = studentService.getStudentResultBYGradeLevelAndSemester(
                account,gradeLevel,semester);

        // Return the grades or a 404 status if the list is empty
        if (grades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(grades);
        } else {
            return ResponseEntity.ok(grades);
        }
    }
    @GetMapping("viewProfile")
    public ResponseEntity<?> getStudent(@AuthenticationPrincipal Account account) {
        try {
            Student student = studentService.getStudentInfo(account);
            return ResponseEntity.ok(student);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found for the given account.");
        }
    }
    @Autowired
    private RegisterService registerService;
    @PutMapping("/register")
    public  ResponseEntity<String>registerStudent(
            @AuthenticationPrincipal Account account){

            String student = registerService.registerExistedStudent(account);
            System.out.println("\n=====================\n");
            System.out.println("\n students is " + student + "\n");
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
    }
    @Autowired
    private RankService rankService;
    @GetMapping("/viewRank")
    public ResponseEntity<List<?>>getRanks(
            @AuthenticationPrincipal Account account
    ){
        List<?>ranks=rankService.getStudentRank(account);
        return ResponseEntity.ok(ranks);
    }
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadProfileImage(
            @AuthenticationPrincipal Account account,
            @RequestParam("file") MultipartFile file) {
        try {
            // Fetch the User associated with the logged-in Account
            Student student = studentRepository.findByAccount(account)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Store the file and update the profileImageUrl
            String filePath = fileStorageService.storeFile(file);
            student.setProfileImageUrl(filePath);
            studentRepository.save(student);

            return ResponseEntity.ok("Profile image uploaded successfully: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }


    @GetMapping("/profileImage")
    public ResponseEntity<?> viewProfileImage(@AuthenticationPrincipal Account account) {
        try {
            String imageUrl = studentService.getUserImage(account);

            // Return the image URL in a structured response
            Map<String, String> response = new HashMap<>();
            response.put("profileImageUrl", imageUrl);

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("User not found for the given account.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while fetching the profile image: " + e.getMessage());
        }
    }




}
