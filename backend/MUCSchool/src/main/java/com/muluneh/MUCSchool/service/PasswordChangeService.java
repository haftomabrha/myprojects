package com.muluneh.MUCSchool.service;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.dto.PassDto;
import com.muluneh.MUCSchool.repository.AccountRepository;
import com.muluneh.MUCSchool.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PasswordChangeService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    @Transactional
    public Account changePassword(PassDto passDto, String username) {
        // Retrieve the account by username
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // Validate the current password using matches() to compare the raw password with the encoded one
        if (!customPasswordEncoder.getPasswordEncoder().matches(passDto.getCurrentPassword(), account.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Encode the new password
        String encodedPassword = customPasswordEncoder.getPasswordEncoder().encode(passDto.getNewPassword());

        // Set the new password on the account
        account.setPassword(encodedPassword);

        // Save the updated account back to the database
        return accountRepository.save(account);
    }
}
