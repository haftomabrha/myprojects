//package com.muluneh.MUCSchool.controller;
//
//import com.muluneh.MUCSchool.domain.Account;
//import com.muluneh.MUCSchool.service.PasswordResetService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/reset-password")
//public class PasswordResetController {
//
//    @Autowired
//    private PasswordResetService passwordResetService;
//
//    // Request password reset
//    @PostMapping("/request")
//    public void requestPasswordReset(@RequestBody String email) {
//        Account account = passwordResetService.findAccountByEmail(email);
//        if (account != null) {
//            String token = passwordResetService.createPasswordResetToken(account);
//            // Send the token to the user's email (implement email service)
//            System.out.println("Reset token: " + token);
//        }
//    }
//
//    // Reset password
//    @PostMapping("/reset")
//    public void resetPassword(@RequestParam String token, @RequestBody String newPassword) {
//        Account account = passwordResetService.validatePasswordResetToken(token);
//        if (account != null) {
//            passwordResetService.resetPassword(account, newPassword);
//        } else {
//            throw new RuntimeException("Invalid or expired token");
//        }
//    }
//}
