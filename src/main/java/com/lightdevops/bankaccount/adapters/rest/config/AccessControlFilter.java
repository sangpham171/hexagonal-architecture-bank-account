package com.lightdevops.bankaccount.adapters.rest.config;

import com.lightdevops.bankaccount.adapters.rest.services.ISecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Configuration
public class AccessControlFilter extends OncePerRequestFilter {
    private final ISecurityService securityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!securityService.checkAuthentication(request)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
