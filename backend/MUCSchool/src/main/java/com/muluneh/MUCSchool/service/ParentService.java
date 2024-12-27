package com.muluneh.MUCSchool.service;

import com.muluneh.MUCSchool.domain.*;
import com.muluneh.MUCSchool.dto.ChildDto;
import com.muluneh.MUCSchool.repository.AccountRepository;
import com.muluneh.MUCSchool.repository.GradeRepository;
import com.muluneh.MUCSchool.repository.ParentRepository;
import com.muluneh.MUCSchool.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParentService {
@Autowired
private AccountRepository accountRepository;
@Autowired
private StudentRepository studentRepository;
@Autowired
private GradeRepository gradeRepository;
@Autowired
private ParentRepository parentRepository;


    @Transactional
    public List<Grade> getStudentResult(String username,Account account) {
        List<Grade> grades=new ArrayList<>();
        Optional<Parent> parentOptional= parentRepository.findByAccount(account);
        Parent parent = parentOptional.get();
        Optional<Account> accountOptional=accountRepository.findByUsername(username);
          Account account1=accountOptional.get();
        Optional<Student> studentOptional=studentRepository.findByAccount(account1);
            Student student = studentOptional.get();

                if (parent.getPhoneNumber().equals(student.getEmergency())) {
                    grades = gradeRepository.findByStudentId(student.getId());
                }
                    // Return the list of grades


                return grades;
        }

    public String getUserImage(Account account) {
        return parentRepository.findByAccount(account)
                .map(Parent::getProfileImageUrl) // Retrieve the profile image URL if the user exists
                .orElse("default-profile-image-url"); // Provide a default value if user is not found
    }
        // Return an empty list if the student is not found

    }



