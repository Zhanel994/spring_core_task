package com.gym.config.logging;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

//generates transactionId for each request
@Component
public class TransactionIdFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(TransactionIdFilter.class);

    private static final String TRANSACTION_ID = "transactionId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String transactionId = UUID.randomUUID().toString();

        MDC.put(TRANSACTION_ID, transactionId);
        response.setHeader("X-Transaction-Id", transactionId);

        logger.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
