package com.example.back_admin.Repository;

import com.example.back_admin.Model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentasRepository extends JpaRepository<Venta,Integer> {
    List<Venta> findByClienteId(Integer clienteId);
}
