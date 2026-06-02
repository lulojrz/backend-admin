package com.example.back_admin.Config;

import com.example.back_admin.Model.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // <-- IMPORTANTE
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
                        // ¡EL ESCUDO MAESTRO!: Permitir todas las peticiones de chequeo CORS (OPTIONS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 1. Rutas Públicas
                        .requestMatchers(
                                "/clientes/login",
                                "/clientes/agregar",
                                "/api/login",
                                "/productos",
                                "/productos/{id}",
                                "/clientes/{id}", 
                                "/confirmar/venta",
                                "/confirmar/detalles",
                                "/api/find/usuarios",
                                "/clientes/email"
                        ).permitAll()

                        // 3. Rutas exclusivas de Empleados
                        .requestMatchers("/productos/variante/{id}").hasRole("EMPLOYEER")
                        .requestMatchers("/confirmar/venta/cliente/{id}","/clientes/editar/{id}","/clientes/verificar/{id}").hasRole("USER")

                        // 4. Rutas exclusivas de ADMIN
                        .requestMatchers(
                                "/api/usuarios",
                                "/api/verificacion/{id}",
                                "/api/registro",
                                "/api/eliminar/{id}",
                                "/api/editar/usuario/{id}", 
                                "/productos/variante",
                                "/productos/categorias",
                                "/clientes/todos",
                                "/confirmar/ventasRealizadas",
                                "/obtenerDetalles/{id}"
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
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173", 
                "http://localhost:5174", 
                "https://ecommerce-lulojrz.netlify.app" // URL limpia corregida
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control")); // Agregado Cache-Control por si las moscas
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
