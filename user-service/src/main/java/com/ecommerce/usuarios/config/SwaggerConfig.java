package com.ecommerce.usuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080"))
                .info(new Info()
                        .title("E-commerce API - Serviço de Usuários")
                        .version("1.0.0")
                        .description("API para gerenciamento de usuários com autenticação JWT")
                        .contact(new Contact()
                                .name("Nome")
                                .email("@email.com")));
    }
}