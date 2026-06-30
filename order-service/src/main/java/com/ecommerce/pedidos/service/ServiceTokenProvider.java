package com.ecommerce.pedidos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceTokenProvider {

    @Value("${service.token}")
    private String serviceToken;

    public String getAuthorizationHeader() {
        return "Bearer " + serviceToken;
    }
}