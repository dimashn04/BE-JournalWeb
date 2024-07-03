package com.trisakti.journalweb.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface CustomFilterChain {

    public void doFilter(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException;

    void setNextFilter(CustomFilterChain secondHandler);
}