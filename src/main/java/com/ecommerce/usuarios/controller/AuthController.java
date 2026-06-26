package com.ecommerce.usuarios.controller;

import com.ecommerce.usuarios.dto.LoginRequest;
import com.ecommerce.usuarios.dto.LoginResponse;
import com.ecommerce.usuarios.dto.UsuarioResponseDTO;
import com.ecommerce.usuarios.model.Usuario;
import com.ecommerce.usuarios.security.JwtService;
import com.ecommerce.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        UsuarioResponseDTO usuario = usuarioService.buscarPorEmail(request.getEmail());

        return ResponseEntity.ok(LoginResponse.builder()
                .token(token)
                .userId(usuario.getId())
                .name(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .build());
    }
}