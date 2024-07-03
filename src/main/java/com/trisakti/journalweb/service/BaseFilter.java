package com.trisakti.journalweb.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public abstract class BaseFilter implements CustomFilterChain {
    private CustomFilterChain nextFilter;

    @Override
    public void setNextFilter(CustomFilterChain nextFilter) {
        this.nextFilter = nextFilter;
    }

    @Override
    public void doFilter(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        if (nextFilter != null) {
            nextFilter.doFilter(request, response);
        }
    }
}
