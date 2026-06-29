package com.ecommerce.usuarios.service;

import com.ecommerce.usuarios.dto.UsuarioRequestDTO;
import com.ecommerce.usuarios.dto.UsuarioResponseDTO;
import com.ecommerce.usuarios.model.Usuario;
import com.ecommerce.usuarios.model.Role;
import com.ecommerce.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private com.ecommerce.usuarios.service.UsuarioService usuarioService;

    @Test
    void deveCriarUsuario() {
        // Dados
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNome("Joao");
        request.setEmail("joao@email.com");
        request.setPassword("123456");

        // Mock
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        when(usuarioRepository.save(any())).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        // Executar
        UsuarioResponseDTO resultado = usuarioService.criar(request);

        // Verificar
        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getEmail());
        verify(usuarioRepository).save(any());
    }

    @Test
    void deveLancarExcecao_QuandoEmailDuplicado() {
        // Dados
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setEmail("joao@email.com");

        // Mock
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Executar e verificar
        assertThrows(RuntimeException.class, () -> usuarioService.criar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveBuscarUsuarioPorId() {
        // Dados
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("joao@email.com");

        // Mock
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Executar
        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1L);

        // Verificar
        assertNotNull(resultado);
        assertEquals("joao@email.com", resultado.getEmail());
    }
}