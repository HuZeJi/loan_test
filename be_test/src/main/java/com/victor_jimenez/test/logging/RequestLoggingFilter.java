package com.victor_jimenez.test.logging;

import com.victor_jimenez.test.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    static final String MDC_TRANSACTION = "transactionId";
    static final String MDC_API = "api";
    static final String MDC_USER_ID = "userId";

    private final JwtService jwtService;

    public RequestLoggingFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String api = request.getMethod() + " " + request.getRequestURI();
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        MDC.put(MDC_TRANSACTION, String.valueOf(uuidAsString));
        MDC.put(MDC_API, api);
        MDC.put(MDC_USER_ID, extractUserId(request));

        long startNanos = System.nanoTime();
        try {
            log.info("APISTART");
            filterChain.doFilter(request, response);
        } finally {
            long responseTimeMs = (System.nanoTime() - startNanos) / 1_000_000;
            log.info("APIEND | RESPONSE_TIME: {}ms | STATUS: {}", responseTimeMs, response.getStatus());
            MDC.remove(MDC_TRANSACTION);
            MDC.remove(MDC_API);
            MDC.remove(MDC_USER_ID);
        }
    }

    private String extractUserId(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return "anonymous";
        }
        try {
            String userId = jwtService.extractUserId(header.substring(BEARER_PREFIX.length()));
            return userId != null ? userId : "anonymous";
        } catch (Exception ex) {
            // Token invalido/expirado - no debe romper el logging, solo queda sin identificar.
            return "anonymous";
        }
    }
}
