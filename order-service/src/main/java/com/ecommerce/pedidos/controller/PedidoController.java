package com.ecommerce.pedidos.controller;

import com.ecommerce.pedidos.dto.EstatisticasProdutoDTO;
import com.ecommerce.pedidos.dto.PedidoResponseDTO;
import com.ecommerce.pedidos.service.PedidoService;
import com.ecommerce.pedidos.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final SecurityUtils securityUtils;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido() {
        String usuarioEmail = securityUtils.getUsuarioLogado();
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(usuarioEmail));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos() {
        String usuarioEmail = securityUtils.getUsuarioLogado();
        return ResponseEntity.ok(pedidoService.listarPedidos(usuarioEmail));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping("/admin/produto/{produtoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoResponseDTO>> buscarPedidosPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(pedidoService.buscarPedidosPorProduto(produtoId));
    }

    @GetMapping("/admin/produto/{produtoId}/estatisticas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstatisticasProdutoDTO> estatisticasProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(pedidoService.estatisticasProduto(produtoId));
    }
}