package com.vishalrashmika.sinso.api.Config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter implements Filter {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    @Value("${rate-limit.capacity:50}")
    private int capacity;
    
    @Value("${rate-limit.refill-tokens:50}")
    private int refillTokens;
    
    @Value("${rate-limit.refill-duration-minutes:1}")
    private int refillDurationMinutes;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String clientIp = getClientIP(httpRequest);
        Bucket bucket = resolveBucket(clientIp);
        
        if (bucket.tryConsume(1)) {
            long availableTokens = bucket.getAvailableTokens();
            httpResponse.setHeader("X-Rate-Limit-Remaining", String.valueOf(availableTokens));
            httpResponse.setHeader("X-Rate-Limit-Limit", "50");
            
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.setContentType("application/json");
            httpResponse.setHeader("X-Rate-Limit-Limit", "50");
            httpResponse.setHeader("X-Rate-Limit-Remaining", "0");
            httpResponse.setHeader("X-Rate-Limit-Retry-After-Seconds", "60");
            
            httpResponse.getWriter().write(
                "{\"error\": \"Too Many Requests\", " +
                "\"message\": \"Rate limit exceeded. Maximum " + capacity + " requests per minute allowed.\", " +
                "\"retryAfter\": \"60 seconds\"}"
            );
        }
    }

    private Bucket resolveBucket(String clientIp) {
        return cache.computeIfAbsent(clientIp, k -> createNewBucket());
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(refillTokens, Duration.ofMinutes(refillDurationMinutes)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Return the first IP in the chain (the original client IP)
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}