package com.ecommerce.pedidos.controller;

import com.ecommerce.pedidos.model.Carrinho;
import com.ecommerce.pedidos.service.CarrinhoService;
import com.ecommerce.pedidos.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinho")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService carrinhoService;
    private final SecurityUtils securityUtils;

    @PostMapping("/adicionar")
    public ResponseEntity<Carrinho> adicionarItem(
            @RequestParam Long produtoId,
            @RequestParam Integer quantidade) {

        String usuarioEmail = securityUtils.getUsuarioLogado();
        Carrinho carrinho = carrinhoService.adicionarItem(usuarioEmail, produtoId, quantidade);
        return ResponseEntity.ok(carrinho);
    }

    @GetMapping
    public ResponseEntity<Carrinho> buscarCarrinho() {
        String usuarioEmail = securityUtils.getUsuarioLogado();
        return ResponseEntity.ok(carrinhoService.buscarCarrinho(usuarioEmail));
    }

    @DeleteMapping
    public ResponseEntity<Void> limparCarrinho() {
        String usuarioEmail = securityUtils.getUsuarioLogado();
        carrinhoService.limparCarrinho(usuarioEmail);
        return ResponseEntity.noContent().build();
    }
}