package com.ecommerce.produtos.service;

import com.ecommerce.produtos.dto.ProdutoRequestDTO;
import com.ecommerce.produtos.dto.ProdutoResponseDTO;
import com.ecommerce.produtos.model.Produto;
import com.ecommerce.produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Notebook Dell");
        produto.setDescricao("Notebook Dell Inspiron i7");
        produto.setPreco(new BigDecimal("4500.00"));
        produto.setEstoque(10);
        produto.setCategoria("Eletrônicos");
        produto.setImagemUrl("https://example.com/notebook.jpg");

        requestDTO = new ProdutoRequestDTO();
        requestDTO.setNome("Notebook Dell");
        requestDTO.setDescricao("Notebook Dell Inspiron i7");
        requestDTO.setPreco(new BigDecimal("4500.00"));
        requestDTO.setEstoque(10);
        requestDTO.setCategoria("Eletrônicos");
        requestDTO.setImagemUrl("https://example.com/notebook.jpg");
    }

    @Test
    void listarTodos_DeveRetornarListaDeProdutos() {
        when(produtoRepository.findAll()).thenReturn(List.of(produto));

        List<ProdutoResponseDTO> resultado = produtoService.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Notebook Dell", resultado.get(0).getNome());
        verify(produtoRepository).findAll();
    }

    @Test
    void buscarPorId_DeveRetornarProduto() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoResponseDTO resultado = produtoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Notebook Dell", resultado.getNome());
        assertEquals(new BigDecimal("4500.00"), resultado.getPreco());
        verify(produtoRepository).findById(1L);
    }

    @Test
    void buscarPorId_ProdutoNaoEncontrado_DeveLancarExcecao() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> produtoService.buscarPorId(99L));
        verify(produtoRepository).findById(99L);
    }

    @Test
    void buscarPorCategoria_DeveRetornarProdutos() {
        when(produtoRepository.findByCategoria("Eletrônicos")).thenReturn(List.of(produto));

        List<ProdutoResponseDTO> resultado = produtoService.buscarPorCategoria("Eletrônicos");

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Eletrônicos", resultado.get(0).getCategoria());
        verify(produtoRepository).findByCategoria("Eletrônicos");
    }

    @Test
    void buscarPorNome_DeveRetornarProdutos() {
        when(produtoRepository.findByNomeContainingIgnoreCase("Notebook")).thenReturn(List.of(produto));

        List<ProdutoResponseDTO> resultado = produtoService.buscarPorNome("Notebook");

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Notebook Dell", resultado.get(0).getNome());
        verify(produtoRepository).findByNomeContainingIgnoreCase("Notebook");
    }

    @Test
    void criar_DeveSalvarProduto() {
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoResponseDTO resultado = produtoService.criar(requestDTO);

        assertNotNull(resultado);
        assertEquals("Notebook Dell", resultado.getNome());
        assertEquals(new BigDecimal("4500.00"), resultado.getPreco());
        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    void atualizar_DeveAtualizarProduto() {
        ProdutoRequestDTO atualizacaoDTO = new ProdutoRequestDTO();
        atualizacaoDTO.setNome("Notebook Dell Atualizado");
        atualizacaoDTO.setDescricao("Notebook Dell Inspiron i7 16GB");
        atualizacaoDTO.setPreco(new BigDecimal("4800.00"));
        atualizacaoDTO.setEstoque(8);
        atualizacaoDTO.setCategoria("Eletrônicos");
        atualizacaoDTO.setImagemUrl("https://example.com/notebook-novo.jpg");

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNome("Notebook Dell Atualizado");
        produtoAtualizado.setDescricao("Notebook Dell Inspiron i7 16GB");
        produtoAtualizado.setPreco(new BigDecimal("4800.00"));
        produtoAtualizado.setEstoque(8);
        produtoAtualizado.setCategoria("Eletrônicos");
        produtoAtualizado.setImagemUrl("https://example.com/notebook-novo.jpg");

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);

        ProdutoResponseDTO resultado = produtoService.atualizar(1L, atualizacaoDTO);

        assertEquals("Notebook Dell Atualizado", resultado.getNome());
        assertEquals(new BigDecimal("4800.00"), resultado.getPreco());
        assertEquals(8, resultado.getEstoque());
        verify(produtoRepository).save(any(Produto.class));
    }

    @Test
    void deletar_DeveChamarRepository() {
        when(produtoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(produtoRepository).deleteById(1L);

        produtoService.deletar(1L);

        verify(produtoRepository).deleteById(1L);
    }

    @Test
    void deletar_ProdutoNaoEncontrado_DeveLancarExcecao() {
        when(produtoRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> produtoService.deletar(99L));
        verify(produtoRepository, never()).deleteById(99L);
    }
}