package com.ecommerce.pedidos.service;

import com.ecommerce.pedidos.dto.ProdutoDTO;
import com.ecommerce.pedidos.model.Carrinho;
import com.ecommerce.pedidos.model.ItemCarrinho;
import com.ecommerce.pedidos.repository.CarrinhoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ProductClient productClient;

    @Transactional
    public Carrinho adicionarItem(String usuarioEmail, Long produtoId, Integer quantidade) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioEmail(usuarioEmail)
                .orElseGet(() -> {
                    Carrinho novo = new Carrinho();
                    novo.setUsuarioEmail(usuarioEmail);
                    return novo;
                });

        ProdutoDTO produto = productClient.buscarProduto(produtoId);

        if (produto == null) {
            throw new RuntimeException("Produto não encontrado!");
        }

        if (produto.getEstoque() < quantidade) {
            throw new RuntimeException("Estoque insuficiente! Disponível: " + produto.getEstoque());
        }

        ItemCarrinho itemExistente = carrinho.getItens().stream()
                .filter(item -> item.getProdutoId().equals(produtoId))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
            itemExistente.setSubtotal(itemExistente.getPrecoUnitario()
                    .multiply(BigDecimal.valueOf(itemExistente.getQuantidade())));
        } else {
            ItemCarrinho novoItem = new ItemCarrinho();
            novoItem.setProdutoId(produtoId);
            novoItem.setProdutoNome(produto.getNome());
            novoItem.setQuantidade(quantidade);
            novoItem.setPrecoUnitario(produto.getPreco());
            novoItem.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(quantidade)));
            carrinho.getItens().add(novoItem);
        }

        return carrinhoRepository.save(carrinho);
    }

    public Carrinho buscarCarrinho(String usuarioEmail) {
        return carrinhoRepository.findByUsuarioEmail(usuarioEmail)
                .orElse(new Carrinho());
    }

    @Transactional
    public void limparCarrinho(String usuarioEmail) {
        Carrinho carrinho = carrinhoRepository.findByUsuarioEmail(usuarioEmail)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado!"));
        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);
    }
}