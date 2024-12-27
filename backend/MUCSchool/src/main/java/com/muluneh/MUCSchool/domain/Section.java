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
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne // A teacher can have multiple sections
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    private String gradeLevel;
    private String section;
}
