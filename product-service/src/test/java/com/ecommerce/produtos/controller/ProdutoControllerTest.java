package com.ecommerce.produtos.controller;

import com.ecommerce.produtos.dto.ProdutoRequestDTO;
import com.ecommerce.produtos.dto.ProdutoResponseDTO;
import com.ecommerce.produtos.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void criarProduto_DeveRetornar201() throws Exception {
        ProdutoRequestDTO request = new ProdutoRequestDTO();
        request.setNome("Notebook Dell");
        request.setDescricao("Notebook Dell Inspiron i7");
        request.setPreco(new BigDecimal("4500.00"));
        request.setEstoque(10);
        request.setCategoria("Eletrônicos");
        request.setImagemUrl("https://example.com/notebook.jpg");

        ProdutoResponseDTO response = ProdutoResponseDTO.builder()
                .id(1L)
                .nome("Notebook Dell")
                .descricao("Notebook Dell Inspiron i7")
                .preco(new BigDecimal("4500.00"))
                .estoque(10)
                .categoria("Eletrônicos")
                .imagemUrl("https://example.com/notebook.jpg")
                .build();

        when(produtoService.criar(any(ProdutoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void listarProdutos_DeveRetornar200() throws Exception {
        when(produtoService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void criarProduto_ComCustomer_DeveRetornar403() throws Exception {
        ProdutoRequestDTO request = new ProdutoRequestDTO();
        request.setNome("Notebook Dell");
        request.setPreco(new BigDecimal("4500.00"));
        request.setEstoque(10);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void buscarPorId_DeveRetornar200() throws Exception {
        ProdutoResponseDTO response = ProdutoResponseDTO.builder()
                .id(1L)
                .nome("Notebook Dell")
                .preco(new BigDecimal("4500.00"))
                .estoque(10)
                .categoria("Eletrônicos")
                .build();

        when(produtoService.buscarPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorCategoria_DeveRetornar200() throws Exception {
        when(produtoService.buscarPorCategoria("Eletrônicos")).thenReturn(List.of());

        mockMvc.perform(get("/api/produtos/categoria/Eletrônicos"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorNome_DeveRetornar200() throws Exception {
        when(produtoService.buscarPorNome("Notebook")).thenReturn(List.of());

        mockMvc.perform(get("/api/produtos/buscar?nome=Notebook"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void atualizarProduto_DeveRetornar200() throws Exception {
        ProdutoRequestDTO request = new ProdutoRequestDTO();
        request.setNome("Notebook Dell Atualizado");
        request.setDescricao("Notebook Dell Inspiron i7 16GB");
        request.setPreco(new BigDecimal("4800.00"));
        request.setEstoque(8);
        request.setCategoria("Eletrônicos");
        request.setImagemUrl("https://example.com/notebook-novo.jpg");

        ProdutoResponseDTO response = ProdutoResponseDTO.builder()
                .id(1L)
                .nome("Notebook Dell Atualizado")
                .descricao("Notebook Dell Inspiron i7 16GB")
                .preco(new BigDecimal("4800.00"))
                .estoque(8)
                .categoria("Eletrônicos")
                .imagemUrl("https://example.com/notebook-novo.jpg")
                .build();

        when(produtoService.atualizar(eq(1L), any(ProdutoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletarProduto_DeveRetornar204() throws Exception {
        doNothing().when(produtoService).deletar(1L);

        mockMvc.perform(delete("/api/produtos/1"))
                .andExpect(status().isNoContent());
    }
}