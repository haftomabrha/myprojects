package com.muluneh.MUCSchool.dto;

import com.muluneh.MUCSchool.domain.Student;
import com.muluneh.MUCSchool.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarkDto {
    private double activity1;
    private double activity2; // Renamed for consistency
    private double test1;
    private double test2;
    private double midExam;
    private double finalExam;

}
