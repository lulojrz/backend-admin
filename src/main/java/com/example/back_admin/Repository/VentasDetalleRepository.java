package com.example.back_admin.Repository;

import com.example.back_admin.Model.DetalleVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VentasDetalleRepository extends JpaRepository<DetalleVentas,Integer> {
    @Query("SELECT d FROM DetalleVentas d WHERE d.venta.id = :idVenta")
    List<DetalleVentas> obtenerDetalleporID(@Param("idVenta") Integer id);
}
