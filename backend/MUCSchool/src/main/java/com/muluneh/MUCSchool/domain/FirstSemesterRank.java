package com.muluneh.MUCSchool.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FirstSemesterRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private Student student;
    private String firstSemester;
    private String gradeLevel;
    private String section;
    private Double totalSum;
    private Double average;
    private  Integer rank;
}
