package com.muluneh.MUCSchool.controller;

import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.dto.GradeDto;
import com.muluneh.MUCSchool.dto.MarkDto;
import com.muluneh.MUCSchool.repository.TeacherRepository;
import com.muluneh.MUCSchool.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/teach")
public class TeacherController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    @GetMapping("/hello")
    @PreAuthorize("hasRole('TEACHER')")
    public String hello() {
        return "this is hello";
    }

    // Endpoint to get a list of students by grade level and section
    @GetMapping("/student") // Use plural form for clarity
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<Student>> getStudentsByGradeLevelAndSection(
            @RequestParam String section,
            @RequestParam String gradeLevel
    ) {
        System.out.println("\n\n\n\n\n\n;");
        System.out.println(" this is form student");
        List<Student> students = studentService.getGradesByGradeLevelAndSections(gradeLevel, section);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @GetMapping("/getstudents")
    public ResponseEntity<?> findStudents(@RequestParam String gradeLevel, @RequestParam String section) {
        System.out.println("in the gradelevel conrollerl");
        List<Student> students = studentService.getGradesByGradeLevelAndSections(gradeLevel, section);
        return ResponseEntity.ok(students);
    }

    // Endpoint to update marks for a specific student
    @PostMapping("/{id}")
    public ResponseEntity<String> updateStudentGrades(
            @AuthenticationPrincipal Account account,
            @RequestBody MarkDto markDto,
            @PathVariable Integer id) {
        try {
            Grade updatedGrade = gradeService.updateGradesForStudent(account, markDto, id);
            return ResponseEntity.ok("Grades updated successfully for student ID: " + updatedGrade.getStudent().getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating grades: " + e.getMessage());
        }

    }


    @GetMapping("/teacherProfile")
    public ResponseEntity<?> getTeacher(@AuthenticationPrincipal Account account) {
        try {
            Teacher teacher = teacherService.getTeacherInfo(account);
            return ResponseEntity.ok(teacher);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found for the given account.");
        }
    }

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/getSchedule")
    public ResponseEntity<List<Schedule>> getTeacherSchedule(@AuthenticationPrincipal Account account) {
        List<Schedule> schedules = scheduleService.getTeacherSchedule(account);
        return ResponseEntity.ok(schedules);
    }

    @Autowired
    private RankService rankService;

    @GetMapping("/doRak")
    public ResponseEntity<List<?>> setRank(
            @RequestParam String semester,
            @AuthenticationPrincipal Account account

    ) {
        List<?> ranks = rankService.setRank(account, semester);
        return ResponseEntity.ok(ranks);
    }
@Autowired
private TeacherRepository teacherRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadProfileImage(
            @AuthenticationPrincipal Account account,
            @RequestParam("file") MultipartFile file) {
        try {
            // Fetch the User associated with the logged-in Account
            Teacher teacher = teacherRepository.findByAccount(account)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Store the file and update the profileImageUrl
            String filePath = fileStorageService.storeFile(file);
            teacher.setProfileImageUrl(filePath);
            teacherRepository.save(teacher);

            return ResponseEntity.ok("Profile image uploaded successfully: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }


    @GetMapping("/profileImage")
    public ResponseEntity<?> viewProfileImage(@AuthenticationPrincipal Account account) {
        try {
            String imageUrl = teacherService.getUserImage(account);

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

