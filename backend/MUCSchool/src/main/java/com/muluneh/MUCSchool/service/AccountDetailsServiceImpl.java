package com.muluneh.MUCSchool.service;

import com.muluneh.MUCSchool.domain.Account;
import com.muluneh.MUCSchool.repository.AccountRepository;
import com.muluneh.MUCSchool.util.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private CustomPasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountropt = accountRepository.findByUsername(username);
        return accountropt.orElseThrow(()-> new UsernameNotFoundException("Invalid credentials"));

    }
}
