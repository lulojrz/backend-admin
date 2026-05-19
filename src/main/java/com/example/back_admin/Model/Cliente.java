package com.example.back_admin.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String rol;
    private String telefono;
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }
    private Integer activo;

}
