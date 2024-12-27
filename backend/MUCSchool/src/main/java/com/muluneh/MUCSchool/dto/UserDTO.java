// src/main/java/com/mekelleuniversity/comunityschool/dto/UserDTO.java

package com.muluneh.MUCSchool.dto;

import com.muluneh.MUCSchool.domain.Subject;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String username;
    private LocalDate birth_date;
    private String gender;
    private String email;
    private String role;

    // You can change this to Role if you prefer to keep the enum
}
