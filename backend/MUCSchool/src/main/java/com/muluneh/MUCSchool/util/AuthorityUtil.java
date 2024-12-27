package com.muluneh.MUCSchool.util;

import com.muluneh.MUCSchool.domain.Account;

public class AuthorityUtil {
    public static  Boolean hasRole(String role, Account account){
        return account.getAuthorities()
                .stream()
                .filter(auth->auth.getAuthority().equals(role))
                .count()>0;
    }
}