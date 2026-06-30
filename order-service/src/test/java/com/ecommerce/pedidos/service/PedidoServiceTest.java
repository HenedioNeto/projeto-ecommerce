package com.ecommerce.pedidos.service;

import com.ecommerce.pedidos.dto.PedidoResponseDTO;
import com.ecommerce.pedidos.dto.ProdutoDTO;
import com.ecommerce.pedidos.model.Carrinho;
import com.ecommerce.pedidos.model.ItemCarrinho;
import com.ecommerce.pedidos.model.Pedido;
import com.ecommerce.pedidos.model.StatusPedido;
import com.ecommerce.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private CarrinhoService carrinhoService;

    @Mock
    private ProductClient productClient;

    @Mock
    private ServiceTokenProvider tokenProvider;

    @InjectMocks
    private PedidoService pedidoService;

    private Carrinho carrinho;
    private ItemCarrinho item;
    private Pedido pedido;
    private ProdutoDTO produtoDTO;

    @BeforeEach
    void setUp() {
        carrinho = new Carrinho();
        carrinho.setUsuarioEmail("joao@email.com");
        carrinho.setItens(new ArrayList<>());

        item = new ItemCarrinho();
        item.setProdutoId(1L);
        item.setProdutoNome("Notebook Dell");
        item.setQuantidade(2);
        item.setPrecoUnitario(new BigDecimal("4500.00"));
        item.setSubtotal(new BigDecimal("9000.00"));
        carrinho.getItens().add(item);

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setUsuarioEmail("joao@email.com");
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setTotal(new BigDecimal("9000.00"));

        produtoDTO = new ProdutoDTO();
        produtoDTO.setId(1L);
        produtoDTO.setNome("Notebook Dell");
        produtoDTO.setPreco(new BigDecimal("4500.00"));
        produtoDTO.setEstoque(10);
        produtoDTO.setCategoria("Eletrônicos");
    }

    @Test
    void criarPedido_DeveCriarPedidoComSucesso() {
        // Mocks
        when(tokenProvider.getAuthorizationHeader()).thenReturn("Bearer 1234567890abcdef");
        when(productClient.buscarProduto(anyLong())).thenReturn(produtoDTO);
        when(carrinhoService.buscarCarrinho("joao@email.com")).thenReturn(carrinho);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        doNothing().when(carrinhoService).limparCarrinho("joao@email.com");

        // Executa
        PedidoResponseDTO resultado = pedidoService.criarPedido("joao@email.com");

        // Verifica
        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getUsuarioEmail());
        assertEquals("PENDENTE", resultado.getStatus());
        assertEquals(new BigDecimal("9000.00"), resultado.getTotal());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void criarPedido_CarrinhoVazio_DeveLancarExcecao() {
        Carrinho carrinhoVazio = new Carrinho();
        carrinhoVazio.setItens(new ArrayList<>());
        when(carrinhoService.buscarCarrinho("joao@email.com")).thenReturn(carrinhoVazio);

        assertThrows(RuntimeException.class, () -> pedidoService.criarPedido("joao@email.com"));
    }

    @Test
    void criarPedido_ProdutoNaoEncontrado_DeveLancarExcecao() {
        when(tokenProvider.getAuthorizationHeader()).thenReturn("Bearer 1234567890abcdef");
        when(productClient.buscarProduto(anyLong())).thenReturn(null);
        when(carrinhoService.buscarCarrinho("joao@email.com")).thenReturn(carrinho);

        assertThrows(RuntimeException.class, () -> pedidoService.criarPedido("joao@email.com"));
    }

    @Test
    void listarPedidos_DeveRetornarLista() {
        when(pedidoRepository.findByUsuarioEmail("joao@email.com")).thenReturn(List.of(pedido));

        List<PedidoResponseDTO> resultado = pedidoService.listarPedidos("joao@email.com");

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("joao@email.com", resultado.get(0).getUsuarioEmail());
    }

    @Test
    void buscarPorId_DeveRetornarPedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        PedidoResponseDTO resultado = pedidoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorId_PedidoNaoEncontrado_DeveLancarExcecao() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pedidoService.buscarPorId(99L));
    }
}