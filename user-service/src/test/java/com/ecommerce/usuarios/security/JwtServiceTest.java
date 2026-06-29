package com.ecommerce.usuarios.security;

import com.ecommerce.usuarios.model.Usuario;
import com.ecommerce.usuarios.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        try {
            var field = JwtService.class.getDeclaredField("secret");
            field.setAccessible(true);
            field.set(jwtService, "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");

            var fieldExp = JwtService.class.getDeclaredField("expiration");
            fieldExp.setAccessible(true);
            fieldExp.set(jwtService, 86400000L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Usuario usuario = new Usuario();
        usuario.setEmail("joao@email.com");
        usuario.setRole(Role.CUSTOMER);
        userDetails = usuario;
    }

    @Test
    void deveGerarEValidarToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);

        String username = jwtService.extractUsername(token);
        assertEquals("joao@email.com", username);

        assertTrue(jwtService.validateToken(token, userDetails));
    }
}