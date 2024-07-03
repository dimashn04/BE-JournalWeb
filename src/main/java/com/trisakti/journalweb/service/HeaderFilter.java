package com.trisakti.journalweb.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class HeaderFilter extends BaseFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String authValue = request.getHeader("Authorization");
        if (!StringUtils.hasText(authValue)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            super.doFilter(request, response);
        }
    }
}