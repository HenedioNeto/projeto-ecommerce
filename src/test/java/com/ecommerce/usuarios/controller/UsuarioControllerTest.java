package com.ecommerce.usuarios.controller;

import com.ecommerce.usuarios.dto.UsuarioRequestDTO;
import com.ecommerce.usuarios.dto.UsuarioResponseDTO;
import com.ecommerce.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void criarUsuario_DeveRetornar201() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNome("Joao Silva");
        request.setEmail("joao@email.com");
        request.setPassword("123456");
        request.setTelefone("11999999999");

        UsuarioResponseDTO response = UsuarioResponseDTO.builder()
                .id(1L)
                .nome("Joao Silva")
                .email("joao@email.com")
                .role("CUSTOMER")
                .build();

        when(usuarioService.criar(any(UsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarUsuarios_DeveRetornar200() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void listarUsuarios_ComCustomer_DeveRetornar403() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void buscarPorId_DeveRetornar200() throws Exception {
        UsuarioResponseDTO response = UsuarioResponseDTO.builder()
                .id(1L)
                .nome("Joao Silva")
                .email("joao@email.com")
                .role("CUSTOMER")
                .build();

        when(usuarioService.buscarPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizarUsuario_DeveRetornar200() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNome("Joao Atualizado");
        request.setEmail("joao@email.com");
        request.setPassword("123456");
        request.setTelefone("11988888888");

        UsuarioResponseDTO response = UsuarioResponseDTO.builder()
                .id(1L)
                .nome("Joao Atualizado")
                .email("joao@email.com")
                .role("CUSTOMER")
                .build();

        when(usuarioService.atualizar(eq(1L), any(UsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletarUsuario_DeveRetornar204() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}