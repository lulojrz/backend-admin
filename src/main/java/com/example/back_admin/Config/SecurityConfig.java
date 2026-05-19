package com.example.back_admin.Config;

import com.example.back_admin.Model.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Rutas Públicas (Siempre con / al principio)
                        .requestMatchers(
                                "/clientes/login",
                                "/clientes/agregar",
                                "/api/login",
                                "/productos",
                                "/productos/{id}"
                        ).permitAll()

                        // 2. Rutas para Clientes Autenticados o Administradores
                        // Agregamos la barra / inicial a confirmar
                        .requestMatchers("/clientes/{id}", "/confirmar/venta").permitAll()

                        // 3. Rutas exclusivas de Empleados
                        .requestMatchers("/productos/variante/{id}").hasRole("EMPLOYEER")

                        // 4. Rutas exclusivas de ADMIN (Agregadas las / faltantes)
                        .requestMatchers(
                                "/api/usuarios",
                                "/api/verificacion/{id}",
                                "/api/registro",
                                "/api/eliminar/{id}",
                                "/api/editar/usuario/{id}", // Agregada /
                                "/api/find/usuarios",
                                "/productos/variante",
                                "/productos/categorias",
                                "/clientes/todos"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:5174"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}