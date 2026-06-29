package com.ecommerce.produtos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();

        System.out.println("🔍 Product Service - Path: " + path + ", Method: " + method);

        // Ignora endpoints públicos (GET)
        if (path.startsWith("/api/produtos") && method.equals("GET")) {
            System.out.println("✅ GET público - ignorando autenticação");
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        System.out.println("🔍 Auth Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Token não encontrado ou inválido");
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        System.out.println("🔍 Token: " + token.substring(0, 20) + "...");

        try {
            final String userEmail = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            System.out.println("🔍 Email: " + userEmail);
            System.out.println("🔍 Role extraída: " + role);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // A role vem como "ROLE_ADMIN" do token
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                System.out.println("🔍 Authorities: " + authorities);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ Autenticação configurada com sucesso!");
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao validar token: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}