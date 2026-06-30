package com.ecommerce.pedidos.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PedidoResponseDTO {
    private Long id;
    private String usuarioEmail;
    private String status;
    private BigDecimal total;
    private LocalDateTime dataCriacao;
    private List<ItemPedidoDTO> itens;
}