package com.vishalrashmika.sinso.api.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitConfiguration {
    
    private int capacity = 50; // Max requests
    private int refillTokens = 50; // Tokens to refill
    private int refillDurationMinutes = 1; // Refill period
    
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRefillTokens() {
        return refillTokens;
    }

    public void setRefillTokens(int refillTokens) {
        this.refillTokens = refillTokens;
    }

    public int getRefillDurationMinutes() {
        return refillDurationMinutes;
    }

    public void setRefillDurationMinutes(int refillDurationMinutes) {
        this.refillDurationMinutes = refillDurationMinutes;
    }
}