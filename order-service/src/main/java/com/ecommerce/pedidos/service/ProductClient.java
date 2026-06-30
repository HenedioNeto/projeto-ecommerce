package com.ecommerce.pedidos.service;

import com.ecommerce.pedidos.dto.ProdutoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "product-service",
        url = "http://localhost:8082"
)
public interface ProductClient {

    @GetMapping("/api/produtos/{id}")
    ProdutoDTO buscarProduto(@PathVariable("id") Long id);

    @PutMapping("/api/produtos/{id}/estoque")
    ProdutoDTO atualizarEstoque(
            @PathVariable Long id,
            @RequestParam Integer quantidade,
            @RequestHeader("Authorization") String authorization);
}