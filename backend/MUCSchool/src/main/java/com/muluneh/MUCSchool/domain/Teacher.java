package com.muluneh.MUCSchool.domain;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Teacher extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String department;
    private String gradeLevel;
    private String section;
    @ManyToOne
    private Subject subject;
}