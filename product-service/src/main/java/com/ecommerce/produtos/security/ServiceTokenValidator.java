package com.ecommerce.produtos.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceTokenValidator {

    @Value("${service.token}")
    private String serviceToken;

    public boolean isValid(String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }
        String extractedToken = token.substring(7);
        boolean isValid = serviceToken.equals(extractedToken);
        return isValid;
    }
}