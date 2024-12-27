package com.muluneh.MUCSchool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private Integer id;
    private String email;
    private String username;
    private Integer emergency;
    private String section;
    private  String gradeLevel;
    private  String stream;
    private  double averageMark;
    private String firstname;
    private String lastname;
    private LocalDate birth_date;
    private String gender;
    private String role;
    private  String semester;
}
