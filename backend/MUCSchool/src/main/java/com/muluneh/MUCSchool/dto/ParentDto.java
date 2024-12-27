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
public class ParentDto {
    private Integer id;
    private String username;
    private String firstname;
    private String lastname;
    private LocalDate birth_date;
    private String gender;
    private String role;
    private Integer phoneNumber;
    private  String email;
}
