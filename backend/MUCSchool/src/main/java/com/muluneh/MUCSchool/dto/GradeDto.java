package com.muluneh.MUCSchool.dto;

import com.muluneh.MUCSchool.domain.Student;
import com.muluneh.MUCSchool.domain.Subject;
import com.muluneh.MUCSchool.domain.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeDto {
    private Integer id;
    private Student student;
    private Teacher teacher;
    private Subject subject;// This could be the teacher entering the grade
    private String semester;
    private String gradeLevel;
    private String field;
    private String sections;

    private double activity1;
    private double activity2; // Renamed for consistency
    private double test1;
    private double test2;
    private double midExam;
    private double finalExam;
}
