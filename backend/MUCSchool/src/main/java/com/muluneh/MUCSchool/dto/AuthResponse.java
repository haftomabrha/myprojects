package com.muluneh.MUCSchool.dto;


import com.muluneh.MUCSchool.domain.Account;

public class AuthResponse {
    private Account account;
    private String token;
    private String welcomeMessage; // New field for the welcome message

    public AuthResponse(Account account, String token, String welcomeMessage) {
        this.account = account;
        this.token = token;
        this.welcomeMessage = welcomeMessage; // Initialize the welcome message
    }

    public Account getAccount() {
        return account;
    }

    public String getToken() {
        return token;
    }

    public String getWelcomeMessage() {
        return welcomeMessage; // Getter for the welcome message
    }
}

