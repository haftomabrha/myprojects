package com.muluneh.MUCSchool.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Entity(name="school_community")

    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String firstname;
        private String lastname;
        private String username;
        private LocalDate birth_date;
        private String gender;
        @OneToOne
        private Account account;
    private String profileImageUrl;

    }