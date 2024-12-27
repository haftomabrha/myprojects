package com.muluneh.MUCSchool.domain;

import com.muluneh.MUCSchool.domain.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Student extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer emergency;
    private String section;
    private  String gradeLevel;
    private  String stream;
    private  double averageMark;
    public Student(Integer studentId){
        id=studentId;
    }

}
