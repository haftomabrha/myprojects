package com.muluneh.MUCSchool.filter;
import com.muluneh.MUCSchool.util.JwtUtil;
import com.muluneh.MUCSchool.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.util.List;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        //Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(header) || (StringUtils.hasText(header) && !header.startsWith("Bearer"))){
            chain.doFilter(request, response);
            return;
        }
        final String token = header.split(" ")[1].trim();
        // Get user identity and set it on the spring security context
        UserDetails userDetails = accountRepository
                .findByUsername(jwtUtil.getUsernameFromToken(token))
                .orElse(null);
        //Get jwt token and validate

        if (!jwtUtil.validateToken(token, userDetails)){
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authotication =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails==null?
                                List.of():userDetails.getAuthorities()
                );
        authotication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        // this where the authetication magic happens and the user is now valide
        SecurityContextHolder.getContext().setAuthentication(authotication);
        chain.doFilter(request, response);
    }
}

