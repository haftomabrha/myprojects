package com.muluneh.MUCSchool.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDto {
    private Integer id;
    private String subjectName;
    private String gradeLevel;
}
