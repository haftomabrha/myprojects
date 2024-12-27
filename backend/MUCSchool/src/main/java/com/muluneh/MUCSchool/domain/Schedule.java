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
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String day;
    private Integer period;
    @ManyToOne // A teacher can have multiple sections
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    private String section;
}
