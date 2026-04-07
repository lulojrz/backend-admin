package com.example.back_admin.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data // Esto genera getters, setters, equals, canEqual, hashCode y toString
@NoArgsConstructor // Necesario para JPA
@AllArgsConstructor // Útil para crear objetos rápido
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String usuario;
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private String rol;
}