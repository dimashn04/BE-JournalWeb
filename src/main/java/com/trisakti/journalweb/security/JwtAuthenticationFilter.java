package com.trisakti.journalweb.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trisakti.journalweb.service.CredentialFilter;
import com.trisakti.journalweb.service.CustomFilterChain;
import com.trisakti.journalweb.service.HeaderFilter;
import com.trisakti.journalweb.service.ValidateTokenFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private UserDetailsService userDetailsService;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        CustomFilterChain customFilterChain = setUpFilterChain();
        customFilterChain.doFilter(request, response);

        if(response.getStatus() != HttpServletResponse.SC_UNAUTHORIZED) {
            String token = getJWTFromRequest(request);
            String username = jwtGenerator.getUsernameFromJwt(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return bearerToken.substring(7, bearerToken.length());
    }

    private CustomFilterChain setUpFilterChain() {
        HeaderFilter firstHandler = new HeaderFilter();
        CredentialFilter secondHandler = new CredentialFilter();
        ValidateTokenFilter thirdHandler = new ValidateTokenFilter();
        firstHandler.setNextFilter(secondHandler);
        secondHandler.setNextFilter(thirdHandler);
        thirdHandler.setTokenGenerator(jwtGenerator);
        return firstHandler;
    }
}
