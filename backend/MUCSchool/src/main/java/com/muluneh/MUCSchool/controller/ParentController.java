package com.muluneh.MUCSchool.controller;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.domain.Grade;
import com.muluneh.MUCSchool.domain.Parent;
import com.muluneh.MUCSchool.domain.User;
import com.muluneh.MUCSchool.dto.ChildDto;
import com.muluneh.MUCSchool.repository.ParentRepository;
import com.muluneh.MUCSchool.service.FileStorageService;
import com.muluneh.MUCSchool.service.GradeService;
import com.muluneh.MUCSchool.service.ParentService;
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
@RequestMapping("api/parent")
public class ParentController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private ParentService parentService;

    @GetMapping("/childrenMark")
    public ResponseEntity<List<Grade>> getStudentGrades(@RequestParam String codedUsername,
                                                        @AuthenticationPrincipal Account account) {
        String username=decodeUsername(codedUsername);
        // Call the service method to get the student's grades
        List<Grade> grades = parentService.getStudentResult(username,account);

            return ResponseEntity.ok(grades);


    }
    public String decodeUsername(String username){
        String usernames=username.replace('r','/');
        String myusername=usernames.replace('r','/');
        return myusername;
    }
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadProfileImage(
            @AuthenticationPrincipal Account account,
            @RequestParam("file") MultipartFile file) {
        try {
            // Fetch the User associated with the logged-in Account
             Parent parent = parentRepository.findByAccount(account)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Store the file and update the profileImageUrl
            String filePath = fileStorageService.storeFile(file);
            parent.setProfileImageUrl(filePath);
            parentRepository.save(parent);

            return ResponseEntity.ok("Profile image uploaded successfully: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }


    @GetMapping("/profileImage")
    public ResponseEntity<?> viewProfileImage(@AuthenticationPrincipal Account account) {
        try {
            String imageUrl = parentService.getUserImage(account);

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
