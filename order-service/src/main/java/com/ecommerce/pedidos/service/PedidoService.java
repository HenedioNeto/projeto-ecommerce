package com.ecommerce.pedidos.service;

import com.ecommerce.pedidos.dto.EstatisticasProdutoDTO;
import com.ecommerce.pedidos.dto.ItemPedidoDTO;
import com.ecommerce.pedidos.dto.PedidoResponseDTO;
import com.ecommerce.pedidos.model.Carrinho;
import com.ecommerce.pedidos.model.ItemCarrinho;
import com.ecommerce.pedidos.model.ItemPedido;
import com.ecommerce.pedidos.model.Pedido;
import com.ecommerce.pedidos.model.StatusPedido;
import com.ecommerce.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarrinhoService carrinhoService;
    private final ProductClient productClient;
    private final ServiceTokenProvider tokenProvider;

    @Transactional
    public PedidoResponseDTO criarPedido(String usuarioEmail) {
        Carrinho carrinho = carrinhoService.buscarCarrinho(usuarioEmail);

        if (carrinho.getItens().isEmpty()) {
            throw new RuntimeException("Carrinho vazio!");
        }

        String authHeader = tokenProvider.getAuthorizationHeader();
        System.out.println("🔍 [ORDER] Token de serviço: " + authHeader);

        for (ItemCarrinho item : carrinho.getItens()) {
            var produto = productClient.buscarProduto(item.getProdutoId());

            if (produto == null) {
                throw new RuntimeException("Produto ID " + item.getProdutoId() + " não encontrado!");
            }

            if (produto.getEstoque() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para: " + produto.getNome() +
                        ". Disponível: " + produto.getEstoque());
            }

            productClient.atualizarEstoque(item.getProdutoId(), item.getQuantidade(), authHeader);
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioEmail(usuarioEmail);
        pedido.setStatus(StatusPedido.PENDENTE);

        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrinho item : carrinho.getItens()) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProdutoId(item.getProdutoId());
            itemPedido.setProdutoNome(item.getProdutoNome());
            itemPedido.setQuantidade(item.getQuantidade());
            itemPedido.setPrecoUnitario(item.getPrecoUnitario());
            itemPedido.setSubtotal(item.getSubtotal());
            pedido.getItens().add(itemPedido);
            total = total.add(item.getSubtotal());
        }

        pedido.setTotal(total);
        Pedido salvo = pedidoRepository.save(pedido);

        carrinhoService.limparCarrinho(usuarioEmail);

        return toResponseDTO(salvo);
    }

    public List<PedidoResponseDTO> listarPedidos(String usuarioEmail) {
        return pedidoRepository.findByUsuarioEmail(usuarioEmail).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));
        return toResponseDTO(pedido);
    }

    public List<PedidoResponseDTO> buscarPedidosPorProduto(Long produtoId) {
        List<Pedido> todosPedidos = pedidoRepository.findAll();

        List<Pedido> pedidosFiltrados = todosPedidos.stream()
                .filter(pedido -> pedido.getItens().stream()
                        .anyMatch(item -> item.getProdutoId().equals(produtoId)))
                .collect(Collectors.toList());

        return pedidosFiltrados.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EstatisticasProdutoDTO estatisticasProduto(Long produtoId) {
        List<Pedido> todosPedidos = pedidoRepository.findAll();

        int quantidadeTotal = 0;
        BigDecimal valorTotal = BigDecimal.ZERO;
        List<String> compradores = new ArrayList<>();
        int numeroDePedidos = 0;
        String produtoNome = "";

        for (Pedido pedido : todosPedidos) {
            for (ItemPedido item : pedido.getItens()) {
                if (item.getProdutoId().equals(produtoId)) {
                    produtoNome = item.getProdutoNome();
                    quantidadeTotal += item.getQuantidade();
                    valorTotal = valorTotal.add(item.getSubtotal());
                    compradores.add(pedido.getUsuarioEmail());
                    numeroDePedidos++;
                    break;
                }
            }
        }

        return EstatisticasProdutoDTO.builder()
                .produtoId(produtoId)
                .produtoNome(produtoNome)
                .quantidadeTotalVendida(quantidadeTotal)
                .valorTotalVendido(valorTotal)
                .numeroDePedidos(numeroDePedidos)
                .compradores(compradores)
                .build();
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .usuarioEmail(pedido.getUsuarioEmail())
                .status(pedido.getStatus().name())
                .total(pedido.getTotal())
                .dataCriacao(pedido.getDataCriacao())
                .itens(pedido.getItens().stream()
                        .map(item -> ItemPedidoDTO.builder()
                                .produtoId(item.getProdutoId())
                                .produtoNome(item.getProdutoNome())
                                .quantidade(item.getQuantidade())
                                .precoUnitario(item.getPrecoUnitario())
                                .subtotal(item.getSubtotal())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}