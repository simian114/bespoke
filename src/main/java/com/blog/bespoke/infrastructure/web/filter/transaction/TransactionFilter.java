package com.blog.bespoke.infrastructure.web.filter.transaction;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TransactionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String trId = TrIdHolder.generateTrId();
            TrIdHolder.setTrId(trId);
            request.setAttribute("transactionId", trId);
            filterChain.doFilter(request, response);
        } finally {
            TrIdHolder.clear();
        }
    }
}
