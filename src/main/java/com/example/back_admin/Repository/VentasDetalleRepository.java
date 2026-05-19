package com.example.back_admin.Repository;

import com.example.back_admin.Model.DetalleVentas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentasDetalleRepository extends JpaRepository<DetalleVentas,Integer> {
}
