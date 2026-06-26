package com.ecommerce.usuarios.repository;

import com.ecommerce.usuarios.model.Usuario;
import com.ecommerce.usuarios.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveSalvarEBuscarUsuario() {
        // Criar
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setPassword("123");
        usuario.setRole(Role.CUSTOMER);
        usuarioRepository.save(usuario);

        // Buscar
        Optional<Usuario> encontrado = usuarioRepository.findByEmail("teste@email.com");

        // Verificar
        assertTrue(encontrado.isPresent());
        assertEquals("Teste", encontrado.get().getNome());
    }

    @Test
    void deveVerificarSeEmailExiste() {
        // Criar
        Usuario usuario = new Usuario();
        usuario.setNome("Joao");
        usuario.setEmail("joao@email.com");
        usuario.setPassword("123");
        usuario.setRole(Role.CUSTOMER);
        usuarioRepository.save(usuario);

        // Verificar
        assertTrue(usuarioRepository.existsByEmail("joao@email.com"));
        assertFalse(usuarioRepository.existsByEmail("naoexiste@email.com"));
    }
}