package com.example.back_admin.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@Data
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private  Integer id;
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;




    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;


    /*@PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }*/

    private String metodoPago;

    private BigDecimal montoTotal;
    private String direccion;
    private String ciudad;
    private String cp;




}
