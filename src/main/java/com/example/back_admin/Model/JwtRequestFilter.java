package com.example.back_admin.Model; // O el paquete donde lo tengas

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. SIEMPRE imprimimos para saber si la petición llegó al servidor
        System.out.println(">>> Interceptando: " + request.getMethod() + " " + request.getRequestURI());

        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // 2. Extraer Token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                System.out.println("DEBUG: Usuario en token -> " + username);
            } catch (Exception e) {
                System.out.println("DEBUG: Error al procesar token -> " + e.getMessage());
            }
        }

        // 3. Autenticar si el token es válido
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt, username)) {


                // Forma correcta de pasar varios roles manualmente
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(
                                new SimpleGrantedAuthority("ROLE_USER"),
                                new SimpleGrantedAuthority("ROLE_ADMIN"),
                                new SimpleGrantedAuthority("ROLE_EMPLOYEER")
                        )
                );


                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("DEBUG: Autenticación exitosa para " + username);
            }
        }

        // 4. CONTINUAR SIEMPRE (Si no pones esto, la respuesta vuelve vacía)
        chain.doFilter(request, response);
    }
}