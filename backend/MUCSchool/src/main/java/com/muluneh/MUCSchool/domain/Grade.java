package com.muluneh.MUCSchool.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Teacher teacher; // This could be the teacher entering the grade

    private String semester;
    private String gradeLevel;
    private String field;
    private String sections; // Renamed for consistency

    private double activity1;
    private double activity2; // Renamed for consistency
    private double test1;
    private double test2;
    private double midExam;
    private double finalExam;

    private double totalSum; // Can be calculated based on other fields

    // Calculate totalSum automatically before persisting
    @PrePersist
    @PreUpdate
    public void calculateTotalSum() {
        this.totalSum = activity1 + activity2 + test1 + test2 + midExam + finalExam;
    }
}
