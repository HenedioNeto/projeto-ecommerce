package com.ecommerce.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PedidoRequestDTO {
    @NotBlank
    private String usuarioEmail;
}