package com.trisakti.journalweb.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import com.trisakti.journalweb.security.JwtGenerator;

import java.io.IOException;

@Service
public class ValidateTokenFilter extends BaseFilter {

    private JwtGenerator tokenGenerator;

    public void setTokenGenerator(JwtGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String authToken = request.getHeader("Authorization");
        String bearerToken = authToken.substring(7, authToken.length());
        if (!tokenGenerator.validateToken(bearerToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        } else {
            super.doFilter(request, response);
        }
    }
}