//package com.muluneh.MUCSchool.service;
//
//import com.muluneh.MUCSchool.domain.Account;
//import com.muluneh.MUCSchool.domain.PasswordReset;
//import com.muluneh.MUCSchool.repository.AccountRepository;
//import com.muluneh.MUCSchool.repository.PasswordResetRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//public class PasswordResetService {
//    @Autowired
//    private AccountRepository accountRepository;
//
//
//    @Autowired
//    private PasswordResetRepository passwordResetRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public String createPasswordResetToken(Account account) {
//        String token = UUID.randomUUID().toString();
//        PasswordReset passwordReset = new PasswordReset();
//        passwordReset.setToken(token);
//        passwordReset.setAccount(account);
//        passwordReset.setExpiryDate(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
//        tokenRepository.save(passwordReset);
//        return token;
//    }
//
//    public Account validatePasswordResetToken(String token) {
//        PasswordReset passwordResetToken = tokenRepository.findByToken(token);
//        if (passwordResetToken != null && passwordResetToken.getExpiryDate().isAfter(LocalDateTime.now())) {
//            return passwordResetToken.getAccount();
//        }
//        return null; // Invalid or expired token
//    }
//
//    public void resetPassword(Account account, String newPassword) {
//        account.setPassword(passwordEncoder.encode(newPassword));
//        accountRepository.save(account);
//    }
//
//    public Account findAccountByEmail(String email) {
//        return  passwordResetRepository.findByToken(token);
//    }
//}
