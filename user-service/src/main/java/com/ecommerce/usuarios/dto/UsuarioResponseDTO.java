package com.ecommerce.usuarios.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String role;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
