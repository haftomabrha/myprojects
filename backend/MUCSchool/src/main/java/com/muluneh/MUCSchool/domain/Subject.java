package com.muluneh.MUCSchool.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table()
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String subjectName;
    private String gradeLevel;

    public Subject(Integer subjectId) {
        id=subjectId;
    }
}
