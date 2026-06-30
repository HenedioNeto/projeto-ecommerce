package com.ecommerce.pedidos.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class EstatisticasProdutoDTO {
    private Long produtoId;
    private String produtoNome;
    private Integer quantidadeTotalVendida;
    private BigDecimal valorTotalVendido;
    private Integer numeroDePedidos;
    private List<String> compradores;
}