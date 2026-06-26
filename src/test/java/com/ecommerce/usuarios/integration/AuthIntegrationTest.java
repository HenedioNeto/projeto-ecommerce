package com.ecommerce.usuarios.integration;

import com.ecommerce.usuarios.dto.LoginRequest;
import com.ecommerce.usuarios.dto.UsuarioRequestDTO;
import com.ecommerce.usuarios.model.Usuario;
import com.ecommerce.usuarios.model.Role;
import com.ecommerce.usuarios.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        Usuario usuario = new Usuario();
        usuario.setNome("Joao Silva");
        usuario.setEmail("joao@email.com");
        usuario.setPassword(passwordEncoder.encode("123456"));
        usuario.setRole(Role.CUSTOMER);
        usuarioRepository.save(usuario);
    }

    @Test
    void login_ComCredenciaisValidas_DeveRetornarToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("joao@email.com");
        loginRequest.setPassword("123456");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_ComSenhaInvalida_DeveRetornar403() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("joao@email.com");
        loginRequest.setPassword("senha_errada");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void registrar_ComDadosValidos_DeveCriarUsuario() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNome("Maria Santos");
        request.setEmail("maria@email.com");
        request.setPassword("123456");
        request.setTelefone("11977777777");

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}