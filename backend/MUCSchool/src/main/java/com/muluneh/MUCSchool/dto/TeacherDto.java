package com.muluneh.MUCSchool.dto;

import com.muluneh.MUCSchool.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDto {
    private String firstname;
    private String lastname;
    private String username;
    private LocalDate birth_date;
    private String gender;
    private String role;
    private String email;
    private String department;
    private String gradeLevel;
    private String section;
    private Integer subject;

}
