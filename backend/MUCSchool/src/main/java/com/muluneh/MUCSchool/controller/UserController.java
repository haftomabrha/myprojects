package com.muluneh.MUCSchool.controller;

import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.dto.*;
import com.muluneh.MUCSchool.repository.AccountRepository;
import com.muluneh.MUCSchool.repository.PasswordResetRepository;
import com.muluneh.MUCSchool.repository.UserRepository;
import com.muluneh.MUCSchool.service.*;
import com.muluneh.MUCSchool.util.CustomPasswordEncoder;
import com.muluneh.MUCSchool.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/auth")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordChangeService passwordChangeService;
    @Autowired
    private UserService userService; // Add UserService for registration

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthCredentialRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(), request.getPassword()
                            )
                    );

            Account account = (Account) authenticate.getPrincipal();
            account.setPassword(null);

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtUtil.generateToken(account)
                    )
                    .body(account);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(
            @RequestBody PassDto passDto,
            @AuthenticationPrincipal Account account
    ) {
        try {
            // Manually validate that passwords are not empty
            if (passDto.getCurrentPassword() == null || passDto.getCurrentPassword().isEmpty() ||
                    passDto.getNewPassword() == null || passDto.getNewPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords cannot be empty");
            }

            String username = account.getUsername();
            passwordChangeService.changePassword(passDto, username);

            return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while changing the password");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editAccount")
    public ResponseEntity<UserDTO> editAccount(
            @RequestBody UserDTO userDTO,
            @AuthenticationPrincipal Account account
    ) {
        UserDTO updatedUser = userService.editAccount(userDTO);
        return ResponseEntity.ok(updatedUser);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(
            @RequestBody UserDTO userDTO,
            @AuthenticationPrincipal Account account
    ) {
        userService.deleteAccount(userDTO);
        return ResponseEntity.ok("Account deleted successfully.");
    }
    @Autowired
    private ScheduleService scheduleService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/scheduling")
    public ResponseEntity<List<Schedule>> setSchedule(@RequestBody ScheduleDto scheduleDto,
                                                      @AuthenticationPrincipal Account account){
        List<Schedule> schedules=scheduleService.setSchedule(scheduleDto);
        return ResponseEntity.ok(schedules);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/fetchTeachers")
    public ResponseEntity<List<Section>> setSchedule(@AuthenticationPrincipal Account account){
        List<Section> sections=scheduleService.getTeachersAndSections();
        return ResponseEntity.ok(sections);
    }





    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createAccount")
    public ResponseEntity<Map<String, String>> CreateAccount(
            @RequestBody UserDTO userDTO,
            @AuthenticationPrincipal Account account) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.adminCreateAccounts(userDTO);
            response.put("message", "User registered successfully!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/teacherAccount")
    public ResponseEntity<Map<String, String>> CreateAccount(
            @RequestBody TeacherDto teacherDto,
            @AuthenticationPrincipal Account account) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.teacherAccount(teacherDto);
            response.put("message", "teacher registered successfully!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editTeacher")
    public ResponseEntity<Map<String, String>> editTeacher(
            @RequestBody TeacherDto teacherDto,
            @AuthenticationPrincipal Account account) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.editTeacher(teacherDto);
            response.put("message", "teacher updated successfully!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        catch (IllegalArgumentException ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException ex) {
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile")
    public ResponseEntity<User> receiveUsername(@AuthenticationPrincipal Account account) {
        String username = account.getUsername();
        User userProfile = userService.getStudentProfile(username);
        return ResponseEntity.ok(userProfile);
    }
    @GetMapping("/AllTeachers")
    public ResponseEntity<List<Teacher>> getAllTeacher(
            @AuthenticationPrincipal Account account

    ) {
       List <Teacher> teachers = userService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/AllStudents")
    public ResponseEntity<List<Student>> getAllStudent(
            @AuthenticationPrincipal Account account
    ) {
        List <Student> students = userService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/AllParents")
    public ResponseEntity<List<Parent>> getAllParent(
            @AuthenticationPrincipal Account account

    ) {
        List <Parent> parents = userService.getAllParent();
        return ResponseEntity.ok(parents);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/AllUsers")
    public ResponseEntity<List<User>> getAllUsers(
            @AuthenticationPrincipal Account account
    ) {
        List <User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @Autowired
    private FileStorageService fileStorageService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadProfileImage(
            @AuthenticationPrincipal Account account,
            @RequestParam("file") MultipartFile file) {
        try {
            // Fetch the User associated with the logged-in Account
            User user = userRepository.findByAccount(account)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Store the file and update the profileImageUrl
            String filePath = fileStorageService.storeFile(file);
            user.setProfileImageUrl(filePath);
            userRepository.save(user);

            return ResponseEntity.ok("Profile image uploaded successfully: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profileImage")
    public ResponseEntity<?> viewProfileImage(@AuthenticationPrincipal Account account) {
        try {
            String imageUrl = userService.getUserImage(account);

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





    @GetMapping("/validate")
    public ResponseEntity<?> validate(@RequestParam String token,
                                      @AuthenticationPrincipal Account account) {
        try {
            Boolean isTokenValid = jwtUtil.validateToken(token, account);
            return ResponseEntity.ok(isTokenValid);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.ok(false);
        }
    }


@Autowired
private AccountRepository accountRepository;
    @Autowired
    private PasswordResetRepository passwordResetRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CustomPasswordEncoder passwordEncoder;


    @PostMapping("/passwordResetRequest")
    public ResponseEntity<String> requestPasswordReset(@RequestBody String email) {
        Optional<Account> account =accountRepository .findByEmail(email);
        if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        // Generate a secure reset token (e.g., UUID)
        String resetToken = UUID.randomUUID().toString();

        // Save the token and expiration time in the database (or cache)
        PasswordReset token = new PasswordReset();
        token.setToken(resetToken);
        token.setAccount(account.get());
        token.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetRepository.save(token);

        // Send reset link via email (replace URL with your frontend's reset page)
        String resetLink = "http://localhost:3000/resetpassword?token=" + resetToken;
        emailService.sendEmail(email, "Password Reset Request", "Click here to reset your password: " + resetLink);

        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }




    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetRequest) {
        Optional<PasswordReset> passwordReset = passwordResetRepository.findByToken(resetRequest.getToken());
        if (passwordReset.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid or expired token.");
        }

        PasswordReset tokenData = passwordReset.get();
        if (tokenData.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired.");
        }

        // Retrieve associated account
        Account account = tokenData.getAccount();

        // Update password (hash it before saving)
        String hashedPassword = passwordEncoder.getPasswordEncoder().encode(resetRequest.getNewPassword());
        account.setPassword(hashedPassword);
        accountRepository.save(account);

        // Remove the token after successful password reset
        passwordResetRepository.delete(tokenData);

        return ResponseEntity.ok("Password has been successfully reset.");
    }







}
