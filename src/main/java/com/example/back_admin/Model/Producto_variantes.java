package com.example.back_admin.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "producto_variantes")
public class Producto_variantes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    private String talla;
    private String color;
    private Integer stock;

    @Column(unique = true)
    private String sku;

    public String getImagen_especifica() {
        return imagen_especifica;
    }

    public void setImagen_especifica(String imagen_especifica) {
        this.imagen_especifica = imagen_especifica;
    }

    @Column(name = "imagen_especifica")
    private String imagen_especifica;

    public BigDecimal getPrecioEspecifico() {
        return precioEspecifico;
    }

    public void setPrecioEspecifico(BigDecimal precioEspecifico) {
        this.precioEspecifico = precioEspecifico;
    }

    @Column(name = "precioEspecifico")
    private BigDecimal precioEspecifico;




    public Producto_variantes() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
}