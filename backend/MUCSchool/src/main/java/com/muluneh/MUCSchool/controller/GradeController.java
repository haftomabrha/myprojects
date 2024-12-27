package com.muluneh.MUCSchool.controller;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.domain.Grade;
import com.muluneh.MUCSchool.domain.Student;
import com.muluneh.MUCSchool.dto.AddMarkRequest;
import com.muluneh.MUCSchool.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/teach")
public class GradeController {

    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/studentsByGrade")
    public ResponseEntity<List<Grade>> getStudentsByGrade(
            @RequestParam Integer subjectId,
            @RequestParam String sections,
            @AuthenticationPrincipal Account account) {
        List<Grade> grades = gradeService.getStudentsBySubjectAndSection(subjectId, sections,account);
        return ResponseEntity.ok(grades);
    }
    @GetMapping("/studentsBySubject")
    public List<Student> getStudentsBySubject(@RequestParam Integer subjectId) {
        return gradeService.getStudentsBySubject(subjectId);
    }

    @PutMapping("/addMark")
    public ResponseEntity<String> addMark(
            @RequestBody AddMarkRequest request,
            @AuthenticationPrincipal Account account) {

        Grade grade = gradeService.addMark(request,account);
        return ResponseEntity.ok("Marks submitted successfully for student ID: " + grade.getStudent().getId());
    }
}
