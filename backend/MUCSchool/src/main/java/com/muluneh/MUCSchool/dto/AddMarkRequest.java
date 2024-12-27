package com.muluneh.MUCSchool.dto;

import lombok.Data;

@Data
public class AddMarkRequest {
    private Integer studentId;
    private Integer subjectId;
    private double activity1;
    private double activity2;
    private double test1;
    private double test2;
    private double midExam;
    private double finalExam;
}
