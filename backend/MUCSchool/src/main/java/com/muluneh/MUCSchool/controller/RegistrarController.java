package com.muluneh.MUCSchool.controller;

import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.dto.*;
import com.muluneh.MUCSchool.service.RegistrarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("api/regist")
public class RegistrarController {

    @Autowired
    private RegistrarService registrarService;

    // Endpoint to register a student

    @PostMapping("/registerStudent")
    @PreAuthorize("hasRole('REGISTRAR')")
    public ResponseEntity<?> registerStudent(@RequestBody StudentDto studentDto,
                                             @AuthenticationPrincipal Account accounts) {
        try {
            System.out.println("Registering student");
            Student student = registrarService.createStudentAccount(studentDto);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Student registration failed", e
            );
        }
    }




    // Endpoint to register a parent
    @PreAuthorize("hasRole('REGISTRAR')")
    @PostMapping("/registerParent")
    public ResponseEntity<Parent> registerParent(@RequestBody ParentDto parentDto) {
        Parent parent = registrarService.createParentAccount(parentDto);
        return ResponseEntity.ok(parent);
    }

    // Endpoint to assign section to a teacher


    public String replaceRWithSlash(String formattedUsername) {
        if (formattedUsername == null) {
            return null; // Handle null input
        }
        return formattedUsername.replace('r', '/'); // Replace 'r' with '/'
    }



    // Endpoint to add a new course
    @PostMapping("/addCourse")
    @PreAuthorize("hasRole('REGISTRAR')")
    public ResponseEntity<Subject> addCourse(@RequestBody SubjectDto subjectDto,
                                            @AuthenticationPrincipal Account accounts) {
        Subject course = registrarService.addCourse(subjectDto);
        return ResponseEntity.ok(course);
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('REGISTRAR')")
    public ResponseEntity<?> getAssign(
            @AuthenticationPrincipal Account account,
            @RequestBody SectionDto sectionDto) {
        try {
            Teacher teacher = registrarService.assign(sectionDto);
            return ResponseEntity.ok(teacher);  // Or map to a DTO if needed
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    @PutMapping("/editStudent")
    @PreAuthorize("hasRole('REGISTRAR')")
    public ResponseEntity<StudentDto> editAccount(
            @RequestBody StudentDto studentDto,
            @AuthenticationPrincipal Account account) {
        StudentDto student = registrarService.editStudentInfo(studentDto);
        return ResponseEntity.ok(student);
    }


    @PutMapping("/editParent")
    @PreAuthorize("hasRole('REGISTRAR')")
    public ResponseEntity<Parent> editAccount(
            @RequestBody ParentDto parentDto,
            @AuthenticationPrincipal Account account) {
        Parent parent = registrarService.editParentInfo(parentDto);
        return ResponseEntity.ok(parent);
    }



}
