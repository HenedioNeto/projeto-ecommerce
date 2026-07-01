package com.ecommerce.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/user")
    public Mono<String> userFallback() {
        return Mono.just("Serviço de usuários indisponível. Tente novamente mais tarde.");
    }

    @GetMapping("/product")
    public Mono<String> productFallback() {
        return Mono.just("Serviço de produtos indisponível. Tente novamente mais tarde.");
    }

    @GetMapping("/order")
    public Mono<String> orderFallback() {
        return Mono.just("Serviço de pedidos indisponível. Tente novamente mais tarde.");
    }
}