package com.example.back_admin.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

import static lombok.ToString.*;

@Entity
@Table(name = "detalle_ventas")
@Data

public class DetalleVentas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "variante_id", nullable = false)
    private Producto_variantes variante;
    @ManyToOne
    @JoinColumn(name = "producto_id",nullable = true)
    private Producto producto;


    private Integer cantidad;




}