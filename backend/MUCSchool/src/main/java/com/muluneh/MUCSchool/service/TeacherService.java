package com.muluneh.MUCSchool.service;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.domain.Teacher;
import com.muluneh.MUCSchool.domain.User;
import com.muluneh.MUCSchool.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;



    public Teacher getTeacherInfo(Account account) {
        return teacherRepository.findByAccount(account)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found for the given account"));
    }
    public String getUserImage(Account account) {
        return teacherRepository.findByAccount(account)
                .map(Teacher::getProfileImageUrl) // Retrieve the profile image URL if the user exists
                .orElse("default-profile-image-url"); // Provide a default value if user is not found
    }
}

